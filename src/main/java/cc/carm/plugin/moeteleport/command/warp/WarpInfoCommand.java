package cc.carm.plugin.moeteleport.command.warp;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        String warpName = args[0];
        Player player = (Player) sender;

        WarpInfo info = MoeTeleport.getWarpManager().getWarp(warpName);
        if (info == null) {
            PluginMessages.Warp.NOT_FOUND.send(player);
            return true;
        }

        String ownerName = info.getOwnerName();
        if (ownerName != null) {
            PluginMessages.Warp.INFO_FULL.send(player, warpName, ownerName, info.getLocation().toFlatString());
        } else {
            PluginMessages.Warp.INFO_LOCATION.send(player, warpName, info.getLocation().toFlatString());
        }
        return true;
    }
}
