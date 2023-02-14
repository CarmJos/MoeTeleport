package cc.carm.plugin.moeteleport.listener;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.conf.PluginConfig;
import cc.carm.plugin.moeteleport.teleport.TeleportQueue;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class TeleportListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!PluginConfig.TELEPORTATION.INTERRUPT.MOVE.getNotNull()) return;

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;

        if (from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        TeleportQueue queue = MoeTeleport.getTeleportManager().getQueue(event.getPlayer());
        if (queue == null) return;

        MoeTeleport.getTeleportManager().interruptQueue(queue);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!PluginConfig.TELEPORTATION.INTERRUPT.ATTACK.getNotNull()) return;
        if (!(event.getEntity() instanceof Player)) return;

        TeleportQueue queue = MoeTeleport.getTeleportManager().getQueue((Player) event.getEntity());
        if (queue == null) return;

        MoeTeleport.getTeleportManager().interruptQueue(queue);
    }

    @EventHandler
    public void onSnake(PlayerToggleSneakEvent event) {
        if (!PluginConfig.TELEPORTATION.INTERRUPT.SNAKE.getNotNull()) return;
        TeleportQueue queue = MoeTeleport.getTeleportManager().getQueue(event.getPlayer());
        if (queue == null) return;

        MoeTeleport.getTeleportManager().interruptQueue(queue);
    }

}
