package cc.carm.plugin.moeteleport.teleport.target;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportPlayerTarget implements TeleportTarget {

    protected final @NotNull Player target;

    public TeleportPlayerTarget(@NotNull Player target) {
        this.target = target;
    }

    @Override
    public Location prepare() {
        return target.isOnline() ? target.getLocation() : null;
    }

    @Override
    public String getText() {
        return target.getName();
    }

}
