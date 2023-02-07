package cc.carm.plugin.moeteleport.command.warp;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.manager.TeleportManager;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpTpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length < 1) return false;
        Player player = (Player) sender;

        WarpInfo info = MoeTeleport.getWarpManager().getWarp(args[0]);
        if (info == null) {
            PluginMessages.Warp.NOT_FOUND.send(player);
            return true;
        }

        TeleportManager.teleport(player, info.getLocation(), false);
        return true;
    }

}
