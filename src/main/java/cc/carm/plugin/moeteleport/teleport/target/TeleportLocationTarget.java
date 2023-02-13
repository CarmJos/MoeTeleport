package cc.carm.plugin.moeteleport.teleport.target;

import cc.carm.plugin.moeteleport.conf.location.DataLocation;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeleportLocationTarget implements TeleportTarget {

    private final @NotNull DataLocation location;

    public TeleportLocationTarget(@NotNull DataLocation location) {
        this.location = location;
    }

    public TeleportLocationTarget(@NotNull Location location) {
        this(new DataLocation(location));
    }

    public @NotNull DataLocation getDataLocation() {
        return location;
    }

    public @Nullable Location getLocation() {
        return getDataLocation().getBukkitLocation();
    }

    @Override
    public Location prepare() {
        Location loc = getLocation();
        if (loc == null || !loc.isWorldLoaded()) return null;
        return loc;
    }

    @Override
    public String getText() {
        return getDataLocation().toFlatString();
    }
}
