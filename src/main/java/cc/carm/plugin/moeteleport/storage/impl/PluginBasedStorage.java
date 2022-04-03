package cc.carm.plugin.moeteleport.storage.impl;

import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public abstract class PluginBasedStorage implements DataStorage {

    protected Plugin dependPlugin;

    public PluginBasedStorage(String dependPluginName) {
        this.dependPlugin = Bukkit.getPluginManager().getPlugin(dependPluginName);
    }

    @Override
    public void initialize() throws NullPointerException {
        if (dependPlugin == null) {
            throw new NullPointerException("该存储类型依赖的插件不存在，请检查配置文件");
        }
    }

    public Plugin getDependPlugin() {
        return dependPlugin;
    }

    @Override
    public void shutdown() {
        // 一般啥也不需要我们做
    }

    @Override
    public void saveUserData(@NotNull UserData data) {
        // 一般都由其他插件自行保存，不需要实现。
    }

    @Override
    public void saveWarps(@NotNull Map<String, WarpInfo> warps) {
        // 一般都由其他插件自行保存，不需要实现。
    }

    @Override
    public void setHome(@NotNull UUID uuid, @NotNull String homeName, @NotNull DataLocation homeLocation) {
        // 一般都由其他插件自行保存，不需要实现。
    }

    @Override
    public boolean delHome(@NotNull UUID uuid, @NotNull String homeName) {
        // 一般都由其他插件自行保存，不需要实现。
        return true;
    }

}
