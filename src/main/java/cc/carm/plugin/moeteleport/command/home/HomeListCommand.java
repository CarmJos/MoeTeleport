package cc.carm.plugin.moeteleport.command.home;

import cc.carm.plugin.moeteleport.command.parent.HomeCommands;
import cc.carm.plugin.moeteleport.command.sub.HomeSubCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.storage.UserData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class HomeListCommand extends HomeSubCommand {

    public HomeListCommand(@NotNull HomeCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        Player player = (Player) sender;
        UserData data = getData(player);

        if (data.getHomeLocations().isEmpty()) {
            PluginMessages.HOME.EMPTY.send(sender);
            return null;
        }

        PluginMessages.HOME.LIST.HEADER.send(player);
        data.getHomeLocations().forEach((name, loc) -> PluginMessages.HOME.LIST.OBJECT.send(player, name, loc.toFlatString()));
        return null;
    }
}
