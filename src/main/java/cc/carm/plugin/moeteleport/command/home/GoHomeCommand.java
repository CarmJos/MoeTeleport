package cc.carm.plugin.moeteleport.command.home;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.manager.TeleportManager;
import cc.carm.plugin.moeteleport.model.UserData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GoHomeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
							 @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		UserData data = Main.getUserManager().getData(player);
		String homeName = args.length >= 1 ? args[0] : null;
		Map.Entry<String, DataLocation> locationInfo = data.getHomeLocation(homeName);
		if (locationInfo == null) {
			PluginMessages.Home.NOT_FOUND.sendWithPlaceholders(player);
		} else {
			TeleportManager.teleport(player, locationInfo.getValue());
		}
		return true;
	}


}