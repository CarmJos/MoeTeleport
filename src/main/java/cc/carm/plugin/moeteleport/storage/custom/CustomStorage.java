package cc.carm.plugin.moeteleport.storage.custom;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomStorage implements DataStorage {

    @Override
    @TestOnly
    public void initialize() throws UnsupportedOperationException {
        Main.severe("您选择使用自定义存储，但并没有应用成功。");
        Main.severe("You are using CustomStorage, but not overwrite the methods.");
        throw new UnsupportedOperationException("您选择使用自定义存储，但并没有应用成功。");
    }

    @Override
    @TestOnly
    public void shutdown() {

    }

    @Override
    @TestOnly
    public @Nullable UserData loadData(@NotNull UUID uuid) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("您选择使用自定义存储，但并没有应用成功。");
    }

    @Override
    @TestOnly
    public void saveUserData(@NotNull UserData data) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("您选择使用自定义存储，但并没有应用成功。");
    }

    @Override
    public Map<String, WarpInfo> getWarps() {
        return new HashMap<>();
    }

    @Override
    public void saveWarps(@NotNull Map<String, WarpInfo> warps) {

    }

    @Override
    public void setWarp(@NotNull String name, @NotNull WarpInfo warpInfo) {

    }

    @Override
    public boolean delWarp(@NotNull String name) {
        return true;
    }

    @Override
    public void setHome(@NotNull UUID uuid, @NotNull String homeName, @NotNull DataLocation homeLocation) throws Exception {
    }

    @Override
    public boolean delHome(@NotNull UUID uuid, @NotNull String homeName) {
        return true;
    }

}
