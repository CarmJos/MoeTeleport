package cc.carm.plugin.moeteleport.listener;

import cc.carm.plugin.moeteleport.MoeTeleport;
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
        MoeTeleport.getUserManager().loadData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        MoeTeleport.getRequestManager().cancelAllRequests(player);
        MoeTeleport.getUserManager().unloadData(player.getUniqueId());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (PluginConfig.DEATH_GO_BACK.get()) {
            Player player = event.getEntity();
            MoeTeleport.getUserManager().getData(player).setLastLocation(player.getLocation());
            PluginMessages.Back.DEATH_MESSAGE.send(player);
        }
    }

}
