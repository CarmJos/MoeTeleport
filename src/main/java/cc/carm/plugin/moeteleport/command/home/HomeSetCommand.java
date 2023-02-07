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

public class HomeSetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        UserData data = MoeTeleport.getUserManager().getData(player);
        String homeName = args.length >= 1 ? args[0] : "home";

        if (homeName.length() > 32) {
            // 限定家的名字长度
            PluginMessages.Home.NAME_TOO_LONG.send(sender);
            return true;
        }
        Map.Entry<String, DataLocation> lastHomeLocation = data.getHomeLocation(homeName);

        int maxHome = MoeTeleport.getUserManager().getMaxHome(player);
        if (data.getHomeLocations().size() >= maxHome && lastHomeLocation == null) {
            PluginMessages.Home.OVER_LIMIT.send(sender, maxHome);
            return true;
        }

        data.setHomeLocation(homeName, player.getLocation());
        if (lastHomeLocation != null) {
            PluginMessages.Home.OVERRIDE.send(sender, homeName, lastHomeLocation.getValue().toFlatString());
        } else {
            PluginMessages.Home.SET.send(player, homeName);
        }

        return true;
    }


}