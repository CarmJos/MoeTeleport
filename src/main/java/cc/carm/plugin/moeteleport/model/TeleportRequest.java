package cc.carm.plugin.moeteleport.model;

import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;
import cc.carm.plugin.moeteleport.manager.TeleportManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportRequest {

	final @NotNull Player sender;
	final @NotNull Player receiver;
	final @NotNull RequestType type;

	final long createTime;

	public TeleportRequest(@NotNull Player sender,
						   @NotNull Player receiver,
						   @NotNull RequestType type) {
		this.sender = sender;
		this.receiver = receiver;
		this.type = type;
		this.createTime = System.currentTimeMillis();
	}

	public @NotNull Player getSender() {
		return sender;
	}

	public @NotNull Player getReceiver() {
		return receiver;
	}

	public @NotNull Player getTeleportPlayer() {
		return getType() == RequestType.TPA ? getSender() : getReceiver();
	}

	public @NotNull Location getTeleportLocation() {
		return getType() == RequestType.TPA_HERE ? getSender().getLocation() : getReceiver().getLocation();
	}

	public @NotNull RequestType getType() {
		return type;
	}

	public long getCreateTime() {
		return createTime;
	}

	public long getActiveTime() {
		return System.currentTimeMillis() - getCreateTime();
	}


	/**
	 * 尝试对玩家进行传送
	 *
	 * @return 是否传送成功
	 */
	public boolean tryTeleport(@NotNull Location location) {
		if (!location.isWorldLoaded()) return false;
		if (!TeleportManager.isSafeLocation(location)) {
			PluginMessages.DANGEROUS.sendWithPlaceholders(getTeleportPlayer());
			return false;
		} else {
			TeleportManager.teleport(getTeleportPlayer(), location);
			return true;
		}
	}

	public boolean isExpired() {
		return getActiveTime() > PluginConfig.EXPIRE_TIME.get() * 10000;
	}

	public enum RequestType {
		TPA,
		TPA_HERE
	}


}
