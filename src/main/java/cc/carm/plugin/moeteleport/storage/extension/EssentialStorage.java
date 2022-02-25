package cc.carm.plugin.moeteleport.storage.extension;

import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import cc.carm.plugin.moeteleport.storage.impl.PluginBasedStorage;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class EssentialStorage extends PluginBasedStorage {

    private Essentials essentials;

    public EssentialStorage() {
        super("Essentials");
    }

    @Override
    public boolean initialize() {
        return super.initialize() && (this.essentials = (Essentials) getDependPlugin()) != null;
    }

    @Override
    public @Nullable UserData loadData(@NotNull UUID uuid) {
        return new EssentialUserData(uuid, getEssentials());
    }

    @Override
    public Map<String, WarpInfo> getWarps() {
        Map<String, WarpInfo> warps = new LinkedHashMap<>();

        for (String warpName : getEssentials().getWarps().getList()) {
            try {
                Location warpLocation = getEssentials().getWarps().getWarp(warpName);
                UUID owner = getEssentials().getWarps().getLastOwner(warpName);
                warps.put(warpName, new WarpInfo(warpName,owner, new DataLocation(warpLocation)));
            } catch (Exception ignore) {
            }
        }

        return warps;
    }

    @Override
    public void setWarp(@NotNull String name, @NotNull WarpInfo warpInfo) throws Exception {
        User user = getEssentials().getUser(warpInfo.getOwner());
        Location location = warpInfo.getLocation().getBukkitLocation();
        if (location == null) return;

        if (user == null) {
            getEssentials().getWarps().setWarp(name, location);
        } else {
            getEssentials().getWarps().setWarp(user, name, warpInfo.getLocation().getBukkitLocation());
        }
    }

    @Override
    public boolean delWarp(@NotNull String name) throws Exception {
        boolean has = hasWarp(name);
        getEssentials().getWarps().removeWarp(name);
        return has;
    }

    @Override
    public boolean hasWarp(@NotNull String name) {
        return getEssentials().getWarps().isWarp(name);
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public static class EssentialUserData extends UserData {

        User essentialsUser;

        public EssentialUserData(@NotNull UUID userUUID, Essentials essentials) {
            super(userUUID);
            this.essentialsUser = essentials.getUser(userUUID);
        }

        public User getEssUser() {
            return essentialsUser;
        }

        @Override
        public LinkedHashMap<String, DataLocation> getHomeLocations() {
            LinkedHashMap<String, DataLocation> homes = new LinkedHashMap<>();
            getEssUser().getHomes().forEach(homeName -> {
                Location homeLocation = getEssUser().getHome(homeName);
                if (homeLocation != null) homes.put(homeName, new DataLocation(homeLocation));
            });
            return homes;
        }

        @Override
        public void setHomeLocation(String homeName, Location location) {
            getEssUser().setHome(homeName, location);
        }

        @Override
        public void delHomeLocation(String homeName) {
            try {
                getEssUser().delHome(homeName);
            } catch (Exception ignored) {
            }
        }

        @Override
        public @Nullable Location getLastLocation() {
            return getEssUser().getLastLocation();
        }

        @Override
        public void setLastLocation(@Nullable Location lastLocation) {
            getEssUser().setLastLocation(lastLocation);
        }


    }

}
