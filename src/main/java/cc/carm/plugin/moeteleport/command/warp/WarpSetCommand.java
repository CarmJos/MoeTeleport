package cc.carm.plugin.moeteleport.command.warp;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpSetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        String warpName = args.length >= 1 ? args[0] : player.getName();

        if (warpName.length() > 16) {
            // 限定家的名字长度
            PluginMessages.Warp.NAME_TOO_LONG.send(sender);
            return true;
        }
        WarpInfo info = MoeTeleport.getWarpManager().getWarp(warpName);

        if (!player.isOp() && !player.hasPermission("MoeTeleport.admin")) {
            if (info != null && (info.getOwner() == null || !info.getOwner().equals(player.getUniqueId()))) {
                PluginMessages.Warp.NOT_OWNER.send(sender, warpName);
                return true;
            }

            int maxWarp = MoeTeleport.getUserManager().getMaxWarps(player);
            long currentUsed = MoeTeleport.getUserManager().countUserWarps(player.getUniqueId());
            if (currentUsed >= maxWarp && info == null) {
                PluginMessages.Warp.OVER_LIMIT.send(sender, maxWarp);
                return true;
            }
        }

        MoeTeleport.getWarpManager().setWarpAsync(warpName, player.getUniqueId(), player.getLocation());
        if (info != null) {
            PluginMessages.Warp.OVERRIDE.send(sender, warpName, info.getLocation().toFlatString());
        } else {
            PluginMessages.Warp.SET.send(sender, warpName);
        }

        return true;
    }
}
