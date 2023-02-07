package cc.carm.plugin.moeteleport.command.home;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.conf.location.DataLocation;
import cc.carm.plugin.moeteleport.storage.UserData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HomeDelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length < 1) return false;

        Player player = (Player) sender;
        UserData data = MoeTeleport.getUserManager().getData(player);
        String homeName = args[0];
        Map.Entry<String, DataLocation> locationInfo = data.getHomeLocation(homeName);
        if (locationInfo == null) {
            PluginMessages.Home.NOT_FOUND.send(player);
        } else {
            PluginMessages.Home.REMOVED.send(player, locationInfo.getKey(), locationInfo.getValue().toFlatString());
            data.delHomeLocation(homeName);
        }
        return true;
    }


}