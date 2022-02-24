package cc.carm.plugin.moeteleport.storage.impl;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.UUID;

public class CustomStorage implements DataStorage {

    @Override
    @TestOnly
    public boolean initialize() {
        Main.error("您选择使用自定义存储，但并没有应用成功。");
        Main.error("You are using CustomStorage, but not overwrite the methods.");
        return false;
    }

    @Override
    @TestOnly
    public void shutdown() {

    }

    @Override
    @TestOnly
    public @Nullable UserData loadData(@NotNull UUID uuid) {
        return null;
    }

    @Override
    @TestOnly
    public void saveUserData(@NotNull UserData data) {

    }

    @Override
    public void setHome(@NotNull UUID uuid, @NotNull String homeName, @NotNull DataLocation homeLocation) throws Exception {
    }

    @Override
    public boolean delHome(@NotNull UUID uuid, @NotNull String homeName) {
        return true;
    }

}
