package cc.carm.plugin.moeteleport.command.home;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.command.parent.HomeCommands;
import cc.carm.plugin.moeteleport.command.sub.HomeSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.conf.location.DataLocation;
import cc.carm.plugin.moeteleport.storage.UserData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HomeCreateCommand extends HomeSubCommand {
    public HomeCreateCommand(@NotNull HomeCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        Player player = (Player) sender;
        UserData data = getData(player);
        String homeName = args.length >= 1 ? args[0] : "home";

        if (homeName.length() > 32) {  // 超过家的名字长度限定
            PluginMessages.HOME.NAME_TOO_LONG.send(sender);
            return null;
        }

        Map.Entry<String, DataLocation> lastHomeLocation = data.getHomeLocation(homeName);

        int maxHome = MoeTeleport.getUserManager().getMaxHome(player);
        if (data.getHomeLocations().size() >= maxHome && lastHomeLocation == null) {
            PluginMessages.HOME.OVER_LIMIT.send(sender, maxHome);
            return null;
        }

        data.setHomeLocation(homeName, player.getLocation());
        if (lastHomeLocation != null) {
            PluginMessages.HOME.OVERRIDE.send(sender, homeName, lastHomeLocation.getValue().toFlatString());
        } else {
            PluginMessages.HOME.SET.send(player, homeName);
        }

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return SimpleCompleter.text(args[args.length - 1], "home", sender.getName());
        } else return Collections.emptyList();
    }

}
