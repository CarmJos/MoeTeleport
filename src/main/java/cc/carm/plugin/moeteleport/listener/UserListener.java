package cc.carm.plugin.moeteleport.listener;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.model.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		UserData data = Main.getUserManager().loadData(uuid);
		Main.getUserManager().getUserDataMap().put(uuid, data);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Main.getRequestManager().cancelAllRequests(player);
		Main.getUserManager().getData(player).save(); //保存
		Main.getUserManager().getUserDataMap().remove(player.getUniqueId());
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (PluginConfig.DEATH_GO_BACK.get()) {
			Player player = event.getEntity();
			Main.getUserManager().getData(player).setLastLocation(player.getLocation());
			PluginMessages.DEATH_BACK.send(player);
		}
	}

}
