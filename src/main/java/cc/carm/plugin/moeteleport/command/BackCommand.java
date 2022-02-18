package cc.carm.plugin.moeteleport.command;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.manager.TeleportManager;
import cc.carm.plugin.moeteleport.model.UserData;
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
        UserData data = Main.getUserManager().getData(player);
        if (data.getLastLocation() == null) {
            PluginMessages.NO_LAST_LOCATION.send(player);
            return true;
        }
        TeleportManager.teleport(player, data.getLastLocation(), false);
        return true;
    }

}
