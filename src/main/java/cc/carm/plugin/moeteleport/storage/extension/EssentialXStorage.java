package cc.carm.plugin.moeteleport.storage.extension;

import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.storage.impl.PluginBasedStorage;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.UUID;

public class EssentialXStorage extends PluginBasedStorage {

    public EssentialXStorage() {
        super("Essentials");
    }

    private Essentials essentials;

    @Override
    public boolean initialize() {
        return super.initialize() && (this.essentials = (Essentials) getDependPlugin()) != null;
    }

    @Override
    public @Nullable UserData loadData(@NotNull UUID uuid) {
        return new EssentialUserData(uuid, getEssentials());
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
        public void setLastLocation(@Nullable Location lastLocation) {
            getEssUser().setLastLocation(lastLocation);
        }

        @Override
        public @Nullable Location getLastLocation() {
            return getEssUser().getLastLocation();
        }


    }

}
