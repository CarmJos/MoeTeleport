package cc.carm.plugin.moeteleport.command.completer;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.storage.UserData;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TpRequestCompleter implements TabCompleter {

    List<Integer> indexes;

    public TpRequestCompleter() {
        this(1);
    }

    public TpRequestCompleter(Integer index) {
        this(new Integer[]{index});
    }

    public TpRequestCompleter(Integer[] indexes) {
        this.indexes = Arrays.asList(indexes);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return ImmutableList.of();
        if (args.length >= 1 && indexes.contains(args.length)) {
            UserData data = MoeTeleport.getUserManager().getData((Player) sender);
            return data.getReceivedRequests().keySet().stream()
                    .map(Bukkit::getPlayer).filter(Objects::nonNull).map(HumanEntity::getName)
                    .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                    .limit(10).collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }


}
