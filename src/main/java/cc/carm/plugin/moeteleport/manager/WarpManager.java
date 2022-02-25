package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.UUID;

public class WarpManager {

    public void saveWarps() {
        try {
            MoeTeleport.getStorage().saveWarps();
        } catch (Exception exception) {
            Main.serve("保存地标数据失败，请检查配置文件。");
            exception.printStackTrace();
        }
    }

    public void setWarp(@NotNull String name, @Nullable UUID owner, @NotNull Location location) {
        setWarp(name, owner, new DataLocation(location));
    }

    public void setWarp(@NotNull String name, @Nullable UUID owner, @NotNull DataLocation location) {

        try {
            MoeTeleport.getStorage().setWarp(name, new WarpInfo(name,owner, location));
        } catch (Exception exception) {
            Main.serve("保存地标数据 " + name + " 失败，请检查配置文件。");
            exception.printStackTrace();
        }

    }

    public void setWarpAsync(@NotNull String name, @Nullable UUID owner, @NotNull Location location) {
        Main.getInstance().getScheduler().runAsync(() -> setWarp(name, owner, location));
    }

    public void setWarpAsync(@NotNull String name, @Nullable UUID owner, @NotNull DataLocation location) {
        Main.getInstance().getScheduler().runAsync(() -> setWarp(name, owner, location));
    }

    public void delWarp(@NotNull String name) {
        try {
            MoeTeleport.getStorage().delWarp(name);
        } catch (Exception exception) {
            Main.serve("删除地标数据 " + name + " 失败，请检查配置文件。");
            exception.printStackTrace();
        }
    }

    public void delWarpAsync(@NotNull String name) {
        Main.getInstance().getScheduler().runAsync(() -> delWarp(name));
    }

    public WarpInfo getWarp(@NotNull String name) {
        return listWarps().entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(name))
                .map(Map.Entry::getValue)
                .findFirst().orElse(null);
    }

    public boolean hasWarp(@NotNull String name) {
        return MoeTeleport.getStorage().hasWarp(name);
    }

    @NotNull
    @Unmodifiable
    public Map<String, WarpInfo> listWarps() {
        return ImmutableMap.copyOf(getWarpsMap());
    }

    @NotNull
    protected Map<String, WarpInfo> getWarpsMap() {
        return MoeTeleport.getStorage().getWarps();
    }


}
