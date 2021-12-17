package cc.carm.plugin.moeteleport.command.home;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DelHomeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
							 @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player)) return false;
		if (args.length < 1) return false;

		Player player = (Player) sender;
		UserData data = Main.getUserManager().getData(player);
		String homeName = args[0];
		Map.Entry<String, DataLocation> locationInfo = data.getHomeLocation(homeName);
		if (locationInfo == null) {
			PluginMessages.Home.NOT_FOUND.sendWithPlaceholders(player);
		} else {
			PluginMessages.Home.REMOVED.sendWithPlaceholders(player,
					new String[]{"%(name)", "%(location)"},
					new Object[]{locationInfo.getKey(), locationInfo.getValue().toString()});
			data.delHomeLocation(homeName);
		}
		return true;
	}


}