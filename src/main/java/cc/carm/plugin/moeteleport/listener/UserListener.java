package cc.carm.plugin.moeteleport.listener;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Main.getUserManager().loadData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Main.getRequestManager().cancelAllRequests(player);
        Main.getUserManager().unloadData(player.getUniqueId());
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
