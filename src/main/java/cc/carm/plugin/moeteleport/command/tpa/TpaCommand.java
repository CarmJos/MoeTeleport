package cc.carm.plugin.moeteleport.command.tpa;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.model.TeleportRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player) || args.length < 1) return false;
        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            PluginMessages.NOT_ONLINE.sendWithPlaceholders(player);
            return true;
        }

        if (player == target) {
            // fix #5 - 玩家给自己发送传送请求
            PluginMessages.SELF_REQUEST.sendWithPlaceholders(player);
            return true;
        }

        TeleportRequest request = Main.getUserManager().getData(target).getReceivedRequests().get(player.getUniqueId());
        if (request != null) {
            PluginMessages.Request.DUPLICATE.sendWithPlaceholders(sender,
                    new String[]{"%(player)", "%(expire)"},
                    new Object[]{target.getName(), request.getRemainSeconds()}
            );
            return true;
        }
        if (command.getName().equalsIgnoreCase("tpa")) {
            Main.getRequestManager().sendRequest(player, target, TeleportRequest.RequestType.TPA);
        } else {
            Main.getRequestManager().sendRequest(player, target, TeleportRequest.RequestType.TPA_HERE);
        }

        return true;
    }

}
