package cc.carm.plugin.moeteleport;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.command.alias.AliasCommandManager;
import cc.carm.lib.easyplugin.updatechecker.GHUpdateChecker;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cc.carm.plugin.moeteleport.command.MainCommands;
import cc.carm.plugin.moeteleport.conf.PluginConfig;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.listener.TeleportListener;
import cc.carm.plugin.moeteleport.listener.UserListener;
import cc.carm.plugin.moeteleport.manager.*;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import cc.carm.plugin.moeteleport.storage.StorageMethod;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.particle.utils.ReflectionUtils;

public class Main extends EasyPlugin {
    private static Main instance;

    protected ConfigurationProvider<?> configProvider;
    protected ConfigurationProvider<?> messageProvider;

    protected DataStorage storage;
    protected WarpManager warpManager;
    protected UserManager userManager;
    protected RequestManager requestManager;
    protected TeleportManager teleportManager;
    protected AliasCommandManager commandManager;

    public Main() {
        instance = this;
    }

    @Override
    protected void load() {

        log("加载插件配置文件...");
        this.configProvider = MineConfiguration.from(this, "config.yml");
        this.configProvider.initialize(PluginConfig.class);

        this.messageProvider = MineConfiguration.from(this, "messages.yml");
        this.messageProvider.initialize(PluginMessages.class);

    }

    @Override
    protected boolean initialize() {

        log("初始化存储方式...");
        StorageMethod storageMethod = StorageMethod.read(PluginConfig.STORAGE.METHOD.get());

        try {
            log("	正在使用 " + storageMethod.name() + " 进行数据存储");
            storage = storageMethod.createStorage();
            storage.initialize();
        } catch (Exception ex) {
            severe("初始化存储失败，请检查配置文件。");
            setEnabled(false);
            return false; // 初始化失败，不再继续加载
        }

        log("加载地标管理器...");
        warpManager = new WarpManager();

        log("加载用户管理器...");
        this.userManager = new UserManager();
        if (Bukkit.getOnlinePlayers().size() > 0) {
            log("   加载现有用户数据...");
            this.userManager.loadAll();
        }

        log("加载请求管理器...");
        this.requestManager = new RequestManager(this);

        log("加载传送管理器...");
        this.teleportManager = new TeleportManager(this);

        log("注册监听器...");
        registerListener(new UserListener());
        registerListener(new TeleportListener());

        log("注册指令...");
        registerCommand("MoeTeleport", new MainCommands(this));

        if (PluginConfig.COMMAND.ENABLE.getNotNull()) {
            log("注册简化指令映射...");
            try {
                this.commandManager = new AliasCommandManager(this);
                PluginConfig.COMMAND.ALIAS.getNotNull().forEach(commandManager::register);
            } catch (Exception e) {
                log("注册简化指令失败： " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (PluginConfig.METRICS.getNotNull()) {
            log("启用统计数据...");
            Metrics metrics = new Metrics(this, 14459);
            metrics.addCustomChart(new SimplePie("storage_method", storageMethod::name));
        }

        if (PluginConfig.CHECK_UPDATE.getNotNull()) {
            log("开始检查更新...");
            getScheduler().runAsync(GHUpdateChecker.runner(this));
        } else {
            log("已禁用检查更新，跳过。");
        }

        log("初始化粒子库...");
        ReflectionUtils.setPlugin(this);

        return true;
    }

    @Override
    protected void shutdown() {
        if (PluginConfig.COMMAND.ENABLE.getNotNull() && this.commandManager != null) {
            log("清空简化指令...");
            this.commandManager.unregisterAll();
        }

        log("关闭所有请求...");
        this.requestManager.shutdown();
        this.teleportManager.shutdown();

        log("保存用户数据...");
        this.userManager.unloadAll(true);

        log("保存地标数据...");
        this.warpManager.saveWarps();

        log("终止存储源...");
        this.storage.shutdown();

        log("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);
    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.getNotNull();
    }

    public static void info(String... messages) {
        getInstance().log(messages);
    }

    public static void severe(String... messages) {
        getInstance().error(messages);
    }

    public static void debugging(String... messages) {
        getInstance().debug(messages);
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigurationProvider<?> getConfigProvider() {
        return configProvider;
    }

    public ConfigurationProvider<?> getMessageProvider() {
        return messageProvider;
    }
}
