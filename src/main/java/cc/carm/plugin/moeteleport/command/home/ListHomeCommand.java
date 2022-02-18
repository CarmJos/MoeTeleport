package cc.carm.plugin.moeteleport.command.home;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.model.UserData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ListHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        UserData data = Main.getUserManager().getData(player);
        PluginMessages.Home.HEADER.sendWithPlaceholders(player);
        data.getHomeLocations().forEach((name, loc) -> PluginMessages.Home.LIST_OBJECT
                .sendWithPlaceholders(player,
                        new String[]{"%(id)", "%(location)"},
                        new Object[]{name, loc.toFlatString()}
                ));
        return true;
    }

}