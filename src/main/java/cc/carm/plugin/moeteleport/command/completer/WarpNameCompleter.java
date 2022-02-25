package cc.carm.plugin.moeteleport.command.completer;

import cc.carm.plugin.moeteleport.MoeTeleport;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.stream.Collectors;

public class WarpNameCompleter implements TabCompleter {

    boolean limitOwner;

    public WarpNameCompleter(boolean limitOwner) {
        this.limitOwner = limitOwner;
    }

    @Nullable
    @Override
    public java.util.List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return ImmutableList.of();
        if (args.length == 1) {
            if (limitOwner) {
                Player player = (Player) sender;
                return MoeTeleport.getWarpManager().listWarps().entrySet().stream()
                        .filter(entry -> entry.getValue().getOwner() != null)
                        .filter(entry -> entry.getValue().getOwner().equals(player.getUniqueId()))
                        .map(Map.Entry::getKey)
                        .filter(s -> StringUtil.startsWithIgnoreCase(s, args[0]))
                        .limit(10).collect(Collectors.toList());
            } else {
                return MoeTeleport.getWarpManager().listWarps().keySet().stream()
                        .filter(s -> StringUtil.startsWithIgnoreCase(s, args[0]))
                        .limit(10).collect(Collectors.toList());
            }
        } else {
            return ImmutableList.of();
        }
    }

}