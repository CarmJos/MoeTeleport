package cc.carm.plugin.moeteleport.command.warp;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpDelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length < 1) return false;
        String warpName = args[0];
        Player player = (Player) sender;

        WarpInfo info = MoeTeleport.getWarpManager().getWarp(warpName);
        if (info == null) {
            PluginMessages.Warp.NOT_FOUND.send(player);
            return true;
        }

        if (!player.isOp() && !player.hasPermission("MoeTeleport.admin")) {
            if (info.getOwner() == null || !info.getOwner().equals(player.getUniqueId())) {
                PluginMessages.Warp.NOT_OWNER.send(player);
                return true;
            }
        }

        MoeTeleport.getWarpManager().delWarpAsync(warpName);
        PluginMessages.Warp.REMOVED.send(player, warpName, info.getLocation().toFlatString());

        return true;
    }
}
