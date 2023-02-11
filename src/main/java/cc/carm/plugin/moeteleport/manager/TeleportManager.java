package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.conf.PluginConfig;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.conf.location.DataLocation;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TeleportManager {


    public static void teleport(Player player, DataLocation targetLocation, boolean onlySafety) {
        Location location = targetLocation.getBukkitLocation();
        if (location == null) {
            PluginMessages.TELEPORT.NOT_AVAILABLE.send(player, targetLocation.toFlatString());
        } else {
            teleport(player, location, onlySafety);
        }
    }

    public static void teleport(Player player, Location targetLocation, boolean onlySafety) {
        if (targetLocation.isWorldLoaded()) {
            if (!onlySafety || TeleportManager.isSafeLocation(targetLocation)) {
                MoeTeleport.getUserManager().getData(player).setLastLocation(player.getLocation());
                player.teleport(targetLocation);
                PluginMessages.TELEPORT.TELEPORTING.send(player, new DataLocation(targetLocation).toFlatString());
            } else {
                PluginMessages.TELEPORT.NOT_SAFE.send(player, new DataLocation(targetLocation).toFlatString());
            }
        } else {
            PluginMessages.TELEPORT.NOT_SAFE.send(player, new DataLocation(targetLocation).toFlatString());
        }
    }

    public static boolean isSafeLocation(Location location) {
        Block leg = location.getBlock();
        if (!leg.getType().isAir()) {
            return false; // not transparent (will suffocate)
        }
        Block head = leg.getRelative(BlockFace.UP);
        if (!head.getType().isAir()) {
            return false; // not transparent (will suffocate)
        }
        Block ground = leg.getRelative(BlockFace.DOWN);
        return !PluginConfig.TELEPORTATION.DANGEROUS_TYPES.getNotNull().contains(ground.getType().name());
    }


}
