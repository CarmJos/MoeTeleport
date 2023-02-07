package cc.carm.plugin.moeteleport.storage.extension;

import cc.carm.plugin.moeteleport.conf.location.DataLocation;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import cc.carm.plugin.moeteleport.storage.UserData;
import cc.carm.plugin.moeteleport.storage.impl.PluginBasedStorage;
import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.Modules.Homes.CmiHome;
import com.Zrips.CMI.Modules.Warps.CmiWarp;
import com.Zrips.CMI.Modules.Warps.WarpManager;
import net.Zrips.CMILib.Container.CMILocation;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class CMIStorage extends PluginBasedStorage {

    public CMIStorage() {
        super("CMI");
    }

    @Override
    public @Nullable UserData loadData(@NotNull UUID uuid) {
        return new CMIUserData(uuid);
    }

    @Override
    public Map<String, WarpInfo> getWarps() {
        Map<String, WarpInfo> warps = new LinkedHashMap<>();

        cmi().getWarps().forEach((name, warp) -> warps.put(name, new WarpInfo(name, warp.getCreator(), convert(warp.getLoc()))));

        return warps;
    }

    @Override
    public void setWarp(@NotNull String name, @NotNull WarpInfo warpInfo) throws Exception {
        CmiWarp warp = new CmiWarp(name);

        warp.setLoc(convert(warpInfo.getLocation()));
        warp.setCreator(warpInfo.getOwner());

        cmi().addWarp(warp);
    }

    @Override
    public boolean delWarp(@NotNull String name) throws Exception {
        CmiWarp warp = cmi().getWarp(name);
        if (warp == null) return false;
        cmi().remove(warp);
        return true;
    }

    @Override
    public boolean hasWarp(@NotNull String name) {
        return cmi().getWarp(name) != null;
    }

    protected WarpManager cmi() {
        return CMI.getInstance().getWarpManager();
    }

    public static DataLocation convert(CMILocation loc) {
        return new DataLocation(loc.getWorldName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public static CMILocation convert(DataLocation loc) {
        return new CMILocation(loc.getWorldName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public static class CMIUserData extends UserData {

        CMIUser cmiUser;

        public CMIUserData(@NotNull UUID userUUID) {
            super(userUUID);
            this.cmiUser = CMI.getInstance().getPlayerManager().getUser(userUUID);
        }

        public CMIUser getCMIUser() {
            return cmiUser;
        }

        @Override
        public LinkedHashMap<String, DataLocation> getHomeLocations() {
            LinkedHashMap<String, DataLocation> homes = new LinkedHashMap<>();
            getCMIUser().getHomes().forEach((name, home) -> {
                homes.put(name, convert(home.getLoc()));
            });
            return homes;
        }

        @Override
        public void setHomeLocation(String homeName, Location location) {
            getCMIUser().addHome(new CmiHome(homeName, new CMILocation(location)), true);
        }

        @Override
        public void delHomeLocation(String homeName) {
            try {
                getCMIUser().removeHome(homeName);
            } catch (Exception ignored) {
            }
        }

        @Override
        public @Nullable Location getLastLocation() {
            return getCMIUser().getLastTeleportLocation();
        }

        @Override
        public void setLastLocation(@Nullable Location lastLocation) {
            getCMIUser().setLastTeleportLocation(lastLocation);
        }

    }


}
