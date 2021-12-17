package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TeleportManager {

	public static void teleport(Player player, DataLocation targetLocation) {
		Location location = targetLocation.getBukkitLocation();
		if (location == null) {
			PluginMessages.NOT_AVAILABLE.sendWithPlaceholders(player,
					new String[]{"%(location)"},
					new Object[]{targetLocation.toString()}
			);
		} else {
			teleport(player, location);
		}
	}

	public static void teleport(Player player, Location targetLocation) {
		if (targetLocation.isWorldLoaded()) {
			player.teleport(targetLocation);
			PluginMessages.TELEPORTING.sendWithPlaceholders(player,
					new String[]{"%(location)"},
					new Object[]{new DataLocation(targetLocation).toString()}
			);
		} else {
			PluginMessages.NOT_AVAILABLE.sendWithPlaceholders(player,
					new String[]{"%(location)"},
					new Object[]{new DataLocation(targetLocation).toString()}
			);
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
		return ground.getType().isSolid()
				&& !PluginConfig.DANGEROUS_TYPES.get().contains(ground.getType().name());
	}


}
