package cc.carm.plugin.moeteleport.command.sub;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.command.parent.WarpCommands;
import cc.carm.plugin.moeteleport.manager.WarpManager;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class WarpSubCommand extends SubCommand<WarpCommands> {
    public WarpSubCommand(@NotNull WarpCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    public WarpManager getManager() {
        return MoeTeleport.getWarpManager();
    }

    @NotNull
    @Unmodifiable
    public Map<String, WarpInfo> listWarps() {
        return getManager().listWarps();
    }

    public WarpInfo getWarp(@NotNull String name) {
        return getManager().getWarp(name);
    }

    public List<String> listWarpNames(CommandSender sender, String input, boolean limitOwner) {
        if (limitOwner && sender instanceof Player) {
            Player player = (Player) sender;
            return listWarps().entrySet().stream()
                    .filter(entry -> entry.getValue().getOwner() != null)
                    .filter(entry -> entry.getValue().getOwner().equals(player.getUniqueId()))
                    .map(Map.Entry::getKey)
                    .filter(s -> StringUtil.startsWithIgnoreCase(s, input))
                    .limit(10).collect(Collectors.toList());
        } else {
            return listWarps().keySet().stream()
                    .filter(s -> StringUtil.startsWithIgnoreCase(s, input))
                    .limit(10).collect(Collectors.toList());
        }
    }

}
