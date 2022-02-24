package cc.carm.plugin.moeteleport.model;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserData {

    protected final @NotNull UUID userUUID;

    public boolean enableAutoSelect = false;
    private @Nullable Location lastLocation;
    private final LinkedHashMap<String, DataLocation> homeLocations;

    private final HashSet<UUID/*receiverUUID*/> sentRequests = new HashSet<>(); // 记录发出的请求
    private final ConcurrentHashMap<UUID/*senderUUID*/, TeleportRequest> receivedRequests = new ConcurrentHashMap<>(); // 记录收到的传送请求

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
        Main.getUserManager().editData((storage) -> storage.setHome(userUUID, homeName, new DataLocation(location)));
    }

    public void delHomeLocation(String homeName) {
        Map.Entry<String, DataLocation> lastLocation = getHomeLocation(homeName);
        if (lastLocation != null) getHomeLocations().remove(lastLocation.getKey());
        Main.getUserManager().editData((storage) -> storage.delHome(userUUID, homeName));
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

    public HashSet<UUID> getSentRequests() {
        return sentRequests;
    }

    public ConcurrentHashMap<UUID, TeleportRequest> getReceivedRequests() {
        return receivedRequests;
    }

    public boolean isEnableAutoSelect() {
        return enableAutoSelect;
    }

    public void setEnableAutoSelect(boolean enableAutoSelect) {
        this.enableAutoSelect = enableAutoSelect;
    }

}
