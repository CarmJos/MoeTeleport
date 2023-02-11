package cc.carm.plugin.moeteleport.model;

import cc.carm.plugin.moeteleport.conf.PluginConfig;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportRequest {

    final @NotNull Player sender;
    final @NotNull Player receiver;
    final @NotNull RequestType type;

    final long createTime;

    boolean confirmed; // 当目的地危险时，需要确认

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

    public long getRemainTime() {
        return PluginConfig.REQUEST.EXPIRE_TIME.getNotNull() * 1000 - getActiveTime();
    }

    public long getRemainSeconds() {
        return getRemainTime() / 1000;
    }

    public boolean isExpired() {
        return getActiveTime() > PluginConfig.REQUEST.EXPIRE_TIME.getNotNull() * 1000;
    }

    public enum RequestType {
        TPA,
        TPA_HERE
    }


}
