package cc.carm.plugin.moeteleport.command.home;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.model.UserData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        UserData data = Main.getUserManager().getData(player);
        String homeName = args.length >= 1 ? args[0] : "home";

        int maxHome = Main.getUserManager().getMaxHome(player);
        if (data.getHomeLocations().size() >= maxHome && data.getHomeLocation(homeName) == null) {
            PluginMessages.Home.OVER_LIMIT.sendWithPlaceholders(sender,
                    new String[]{"%(max)"}, new Object[]{maxHome}
            );
            return true;
        }

        data.setHomeLocation(homeName, player.getLocation());
        PluginMessages.Home.SET.sendWithPlaceholders(player,
                new String[]{"%(name)"}, new Object[]{homeName}
        );
        return true;
    }


}