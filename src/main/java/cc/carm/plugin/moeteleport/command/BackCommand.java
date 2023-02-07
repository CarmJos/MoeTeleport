package cc.carm.plugin.moeteleport.command;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.manager.TeleportManager;
import cc.carm.plugin.moeteleport.storage.UserData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BackCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        UserData data = MoeTeleport.getUserManager().getData(player);
        if (data.getLastLocation() == null) {
            PluginMessages.Back.NO_LAST_LOCATION.send(player);
            return true;
        }
        TeleportManager.teleport(player, data.getLastLocation(), false);
        return true;
    }

}
