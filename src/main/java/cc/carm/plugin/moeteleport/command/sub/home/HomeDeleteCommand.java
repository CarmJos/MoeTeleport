package cc.carm.plugin.moeteleport.command.sub.home;

import cc.carm.plugin.moeteleport.command.sub.HomeCommands;
import cc.carm.plugin.moeteleport.command.base.HomeSubCommand;
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

public class HomeDeleteCommand extends HomeSubCommand {

    public HomeDeleteCommand(@NotNull HomeCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        if (args.length < 1) return getParent().noArgs(sender);

        Player player = (Player) sender;
        UserData data = getData(player);
        String homeName = args[0];

        Map.Entry<String, DataLocation> locationInfo = data.getHomeLocation(homeName);
        if (locationInfo == null) {
            PluginMessages.HOME.NOT_FOUND.send(player);
        } else {
            PluginMessages.HOME.REMOVED.send(player, locationInfo.getKey(), locationInfo.getValue().toFlatString());
            data.delHomeLocation(homeName);
        }

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return listHomes(sender, args[args.length - 1]);
        } else return Collections.emptyList();
    }

}
