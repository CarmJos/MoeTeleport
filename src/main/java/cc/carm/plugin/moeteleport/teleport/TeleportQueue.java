package cc.carm.plugin.moeteleport.teleport;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.storage.UserData;
import cc.carm.plugin.moeteleport.teleport.target.TeleportTarget;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class TeleportQueue {

    private final @NotNull Player player;
    private final @NotNull TeleportTarget target;

    private final long createMillis;
    private final long executeMillis;

    public TeleportQueue(@NotNull Player player, @NotNull TeleportTarget target, @Nullable Duration delay) {
        this.player = player;
        this.target = target;
        this.createMillis = System.currentTimeMillis();
        this.executeMillis = delay == null ? 0 : System.currentTimeMillis() + delay.toMillis();
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull UserData getUser() {
        return MoeTeleport.getUserManager().getData(getPlayer());
    }

    public @NotNull TeleportTarget getTarget() {
        return target;
    }

    public long getCreateMillis() {
        return createMillis;
    }

    public long getExecuteMillis() {
        return executeMillis;
    }

    public long getRemainSeconds() {
        return (executeMillis - System.currentTimeMillis()) / 1000;
    }

    public boolean checkTime() {
        return System.currentTimeMillis() >= executeMillis;
    }

}
