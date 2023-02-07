package cc.carm.plugin.moeteleport.storage.impl;

import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.conf.location.DataLocation;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.UUID;

public abstract class FileBasedStorage implements DataStorage {

    protected static final ConfigValue<String> FILE_PATH = ConfiguredValue.builder(String.class)
            .fromString().defaults("data")
            .headerComments(
                    "选择 yaml/json 存储方式时的存储路径",
                    "默认为相对路径，相对于插件生成的配置文件夹下的路径",
                    "支持绝对路径，如 “/var/data/moe-teleport/\"(linux) 或 \"D:\\data\\moe-teleport\\\"(windows)",
                    "使用绝对路径时请注意权限问题"
            ).build();

    protected @Nullable File dataFolder;

    @Override
    public void initialize() throws Exception {
        FILE_PATH.initialize(
                Main.getInstance().getConfigProvider(), true,
                "storage.file", null, null
        );

        this.dataFolder = new File(Main.getInstance().getDataFolder(), FILE_PATH.getNotNull());
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
