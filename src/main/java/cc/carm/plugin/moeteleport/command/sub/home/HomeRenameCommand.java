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

public class HomeRenameCommand extends HomeSubCommand {

    public HomeRenameCommand(@NotNull HomeCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        if (args.length < 2) return getParent().noArgs(sender);

        String homeName = args[0];
        String newName = args[1];

        if (homeName.equals(newName)) return getParent().noArgs(sender);
        if (newName.length() > 32) {  // 超过家的名字长度限定
            PluginMessages.HOME.NAME_TOO_LONG.send(sender);
            return null;
        }

        Player player = (Player) sender;
        UserData data = getData(player);

        Map.Entry<String, DataLocation> locationInfo = data.getHomeLocation(homeName);
        if (locationInfo == null) {
            PluginMessages.HOME.NOT_FOUND.send(player);
            return null;
        }

        Map.Entry<String, DataLocation> newInfo = data.getHomeLocation(newName);
        if (newInfo != null) {
            PluginMessages.HOME.ALREADY_EXITS.send(player);
            return null;
        }

        PluginMessages.HOME.RENAMED.send(player, newName, locationInfo.getKey());

        data.setHomeLocation(newName, locationInfo.getValue().getBukkitLocation());
        data.delHomeLocation(homeName);

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return listHomes(sender, args[args.length - 1]);
        } else return Collections.emptyList();
    }

}
