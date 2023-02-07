package cc.carm.plugin.moeteleport.model;

import cc.carm.plugin.moeteleport.conf.location.DataLocation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WarpInfo {

    private final @NotNull String name;
    private final @Nullable UUID owner;
    private final @NotNull DataLocation location;

    public WarpInfo(@NotNull String name, @Nullable UUID owner, @NotNull DataLocation location) {
        this.name = name;
        this.owner = owner;
        this.location = location;
    }

    public @NotNull String getName() {
        return name;
    }

    public @Nullable UUID getOwner() {
        return owner;
    }

    public @Nullable String getOwnerName() {
        if (getOwner() != null) {
            OfflinePlayer offline = Bukkit.getOfflinePlayer(getOwner());
            if (offline.getName() != null) {
                return offline.getName();
            }
        }
        return null;
    }


    public @NotNull DataLocation getLocation() {
        return location;
    }

}

