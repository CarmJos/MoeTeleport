package cc.carm.plugin.protalcommands.listener;

import cc.carm.plugin.protalcommands.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class UserListener implements Listener {


	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		Location to = event.getTo();
		if (to == null || to.getWorld() == null) {
			return;
		}
		World.Environment environment = to.getWorld().getEnvironment();
		if (environment == World.Environment.NETHER) {
			event.setCancelled(true); //取消传送
			executeCommands(event.getPlayer(), Main.getCommands());
		}
	}

	private static void executeCommands(Player player, List<String> commands) {
		if (commands == null || commands.isEmpty()) return;
		for (String command : commands) {
			try {
				player.chat(PlaceholderAPI.setPlaceholders(player, command));
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}


}
