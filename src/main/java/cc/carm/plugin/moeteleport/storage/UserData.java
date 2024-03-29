package cc.carm.plugin.moeteleport.storage;

import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.conf.location.DataLocation;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class UserData {

    protected final @NotNull UUID userUUID;
    private final LinkedHashMap<String, DataLocation> homeLocations;

    public boolean enableAutoSelect = false;
    private @Nullable Location lastLocation;

    public UserData(@NotNull UUID userUUID) {
        this(userUUID, null, new LinkedHashMap<>());
    }

    public UserData(@NotNull UUID userUUID,
                    @Nullable DataLocation lastLocation,
                    @NotNull LinkedHashMap<String, DataLocation> homeLocations) {
        this.userUUID = userUUID;
        this.lastLocation = Optional.ofNullable(lastLocation).map(DataLocation::getBukkitLocation).orElse(null);
        this.homeLocations = homeLocations;
    }

    public @NotNull UUID getUserUUID() {
        return userUUID;
    }

    public LinkedHashMap<String, DataLocation> getHomeLocations() {
        return homeLocations;
    }

    public void setHomeLocation(String homeName, Location location) {
        delHomeLocation(homeName);
        getHomeLocations().put(homeName, new DataLocation(location));
        MoeTeleport.getUserManager().editData((storage) -> storage.setHome(userUUID, homeName, new DataLocation(location)));
    }

    public void delHomeLocation(String homeName) {
        Map.Entry<String, DataLocation> lastLocation = getHomeLocation(homeName);
        if (lastLocation != null) getHomeLocations().remove(lastLocation.getKey());
        MoeTeleport.getUserManager().editData((storage) -> storage.delHome(userUUID, homeName));
    }

    public Map.Entry<String, DataLocation> getHomeLocation(@Nullable String homeName) {
        LinkedHashMap<String, DataLocation> homes = getHomeLocations();
        if (homeName == null) {
            if (homes.containsKey("home")) {
                return new AbstractMap.SimpleEntry<>("home", homes.get("home"));
            } else {
                return homes.entrySet().stream().findFirst().orElse(null);
            }
        } else {
            return homes.entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(homeName))
                    .findFirst().orElse(null);
        }
    }

    public @Nullable Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(@Nullable Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public boolean isEnableAutoSelect() {
        return enableAutoSelect;
    }

    public void setEnableAutoSelect(boolean enableAutoSelect) {
        this.enableAutoSelect = enableAutoSelect;
    }

}
