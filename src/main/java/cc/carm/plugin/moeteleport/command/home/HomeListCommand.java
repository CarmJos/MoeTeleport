package cc.carm.plugin.moeteleport.command.home;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.model.UserData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        UserData data = MoeTeleport.getUserManager().getData(player);
        PluginMessages.Home.List.HEADER.send(player);
        data.getHomeLocations().forEach((name, loc) -> PluginMessages.Home.List.OBJECT.send(player, name, loc.toFlatString()));
        return true;
    }

}