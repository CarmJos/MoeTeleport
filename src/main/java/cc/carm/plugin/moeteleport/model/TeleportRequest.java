package cc.carm.plugin.moeteleport.model;

import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.manager.TeleportManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportRequest {

	final Player sender;
	final Player receiver;
	/**
	 * 用于记录需要被传送的玩家
	 */
	final Player teleportPlayer;

	final DataLocation targetLocation;

	final long createTime;

	public TeleportRequest(Player sender, Player receiver, Player teleportPlayer,
						   DataLocation targetLocation, long createTime) {
		this.sender = sender;
		this.receiver = receiver;
		this.teleportPlayer = teleportPlayer;
		this.targetLocation = targetLocation;
		this.createTime = createTime;
	}

	public Player getSender() {
		return sender;
	}

	public Player getReceiver() {
		return receiver;
	}

	public Player getTeleportPlayer() {
		return teleportPlayer;
	}

	public DataLocation getTargetLocation() {
		return targetLocation;
	}

	public long getCreateTime() {
		return createTime;
	}

	/**
	 * 尝试对玩家进行传送
	 *
	 * @return 是否传送成功
	 */
	public boolean tryTeleport() {
		Location location = getTargetLocation().getBukkitLocation();
		if (location == null) return false;
		if (!TeleportManager.isSafeLocation(location)) {
			PluginMessages.DANGEROUS.sendWithPlaceholders(getTeleportPlayer());
			return false;
		} else {
			TeleportManager.teleport(getTeleportPlayer(), location);
			return true;
		}
	}

	public boolean isExpired() {
		return (System.currentTimeMillis() - getCreateTime()) > PluginConfig.EXPIRE_TIME.get() * 10000;
	}


}
