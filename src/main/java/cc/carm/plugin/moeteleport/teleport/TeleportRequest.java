package cc.carm.plugin.moeteleport.teleport;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.conf.PluginConfig;
import cc.carm.plugin.moeteleport.storage.UserData;
import cc.carm.plugin.moeteleport.teleport.target.TeleportPlayerTarget;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class TeleportRequest {

    public enum Type {TPA_TO, TPA_HERE}

    private final @NotNull Type type;

    private final @NotNull Player sender;
    private final @NotNull Player receiver;

    private final long createMillis;

    public TeleportRequest(@NotNull Type type, @NotNull Player sender, @NotNull Player receiver) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.createMillis = System.currentTimeMillis();
    }

    public @NotNull Type getRequestType() {
        return type;
    }

    public @NotNull Player getReceiver() {
        return receiver;
    }

    public @NotNull UserData getReceiverUser() {
        return MoeTeleport.getUserManager().getData(getReceiver());
    }

    public @NotNull Player getSender() {
        return sender;
    }

    public @NotNull UserData getSenderUser() {
        return MoeTeleport.getUserManager().getData(getSender());
    }

    public long getCreateMillis() {
        return createMillis;
    }

    public long getActiveMillis() {
        return System.currentTimeMillis() - getCreateMillis();
    }

    public long getRemainMillis() {
        return PluginConfig.REQUEST.EXPIRE_TIME.getNotNull() * 1000 - getActiveMillis();
    }

    public long getRemainSeconds() {
        return getRemainMillis() / 1000;
    }

    public boolean isExpired() {
        return getActiveMillis() > PluginConfig.REQUEST.EXPIRE_TIME.getNotNull() * 1000;
    }

    public @Nullable TeleportQueue createQueue(@Nullable Duration delay) {
        Player player;
        TeleportPlayerTarget destination;

        if (type == Type.TPA_TO) {
            destination = new TeleportPlayerTarget(receiver);
            player = sender;
        } else if (type == Type.TPA_HERE) {
            destination = new TeleportPlayerTarget(sender);
            player = receiver;
        } else {
            return null;
        }

        return new TeleportQueue(player, destination, delay);
    }


}
