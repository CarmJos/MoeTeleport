package cc.carm.plugin.moeteleport.storage.impl;

import cc.carm.lib.easyplugin.configuration.values.ConfigValue;
import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.UUID;

public abstract class FileBasedStorage implements DataStorage {

    private static final ConfigValue<String> FILE_PATH = new ConfigValue<>(
            "storage.file-path", String.class, "data"
    );

    protected @Nullable File dataFolder;

    @Override
    public void initialize() throws Exception {
        this.dataFolder = new File(Main.getInstance().getDataFolder(), FILE_PATH.get());
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdir()) {
                throw new Exception("无法创建数据文件夹！");
            }
        } else if (!dataFolder.isDirectory()) {
            throw new Exception("数据文件夹路径对应的不是一个文件夹！");
        }
    }

    @Override
    public void shutdown() {
        // 似乎没什么需要做的？
        dataFolder = null;
    }

    @Override
    public void setHome(@NotNull UUID uuid, @NotNull String homeName, @NotNull DataLocation homeLocation) throws Exception {
        // saveData 方法即保存所有数据，不需要针对单个数据进行变更。
    }

    @Override
    public boolean delHome(@NotNull UUID uuid, @NotNull String homeName) {
        // saveData 方法即保存所有数据，不需要针对单个数据进行变更。
        return true;
    }

    @Override
    public void setWarp(@NotNull String name, @NotNull WarpInfo warpInfo) {
        // saveWarp 方法即保存所有数据，不需要针对单个数据进行变更。
    }

    @Override
    public boolean delWarp(@NotNull String name) {
        // saveWarp 方法即保存所有数据，不需要针对单个数据进行变更。
        return true;
    }

    public @Nullable File getDataFolder() {
        return dataFolder;
    }

}
