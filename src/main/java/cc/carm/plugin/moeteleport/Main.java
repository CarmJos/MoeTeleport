package cc.carm.plugin.moeteleport;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import cc.carm.plugin.moeteleport.command.BackCommand;
import cc.carm.plugin.moeteleport.command.MoeTeleportCommand;
import cc.carm.plugin.moeteleport.command.completer.HomeNameCompleter;
import cc.carm.plugin.moeteleport.command.completer.PlayerNameCompleter;
import cc.carm.plugin.moeteleport.command.completer.TpRequestCompleter;
import cc.carm.plugin.moeteleport.command.completer.WarpNameCompleter;
import cc.carm.plugin.moeteleport.command.home.HomeDelCommand;
import cc.carm.plugin.moeteleport.command.home.HomeListCommand;
import cc.carm.plugin.moeteleport.command.home.HomeSetCommand;
import cc.carm.plugin.moeteleport.command.home.HomeTpCommand;
import cc.carm.plugin.moeteleport.command.tpa.TpHandleCommand;
import cc.carm.plugin.moeteleport.command.tpa.TpaCommand;
import cc.carm.plugin.moeteleport.command.warp.*;
import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.listener.UserListener;
import cc.carm.plugin.moeteleport.manager.ConfigManager;
import cc.carm.plugin.moeteleport.manager.RequestManager;
import cc.carm.plugin.moeteleport.manager.UserManager;
import cc.carm.plugin.moeteleport.manager.WarpManager;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import cc.carm.plugin.moeteleport.storage.StorageMethod;
import cc.carm.plugin.moeteleport.util.JarResourceUtils;
import cc.carm.plugin.moeteleport.util.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class Main extends EasyPlugin {
    private static Main instance;

    protected DataStorage storage;
    protected WarpManager warpManager;
    protected UserManager userManager;
    protected RequestManager requestManager;

    public Main() {
        super(new EasyPluginMessageProvider.zh_CN());
        instance = this;
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    public static void regListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getInstance());
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

    @Override
    protected boolean initialize() {

        info("加载配置文件...");
        if (!ConfigManager.initConfig()) {
            severe("配置文件初始化失败，请检查。");
            setEnabled(false);
            return false;
        }

        info("初始化存储方式...");
        StorageMethod storageMethod = StorageMethod.read(PluginConfig.STORAGE_METHOD.get());

        try {
            info("	正在使用 " + storageMethod.name() + " 进行数据存储");
            storage = storageMethod.createStorage();
            storage.initialize();
        } catch (Exception ex) {
            severe("初始化存储失败，请检查配置文件。");
            setEnabled(false);
            return false; // 初始化失败，不再继续加载
        }


        info("加载地标管理器...");
        warpManager = new WarpManager();

        info("加载用户管理器...");
        this.userManager = new UserManager();
        if (Bukkit.getOnlinePlayers().size() > 0) {
            info("   加载现有用户数据...");
            this.userManager.loadAll();
        }

        info("加载请求管理器...");
        this.requestManager = new RequestManager(this);

        info("注册监听器...");
        regListener(new UserListener());

        info("注册指令...");
        registerCommand("MoeTeleport", new MoeTeleportCommand());

        registerCommand("back", new BackCommand());

        registerCommand("listHome", new HomeListCommand());
        registerCommand("home", new HomeTpCommand(), new HomeNameCompleter());
        registerCommand("setHome", new HomeSetCommand());
        registerCommand("delHome", new HomeDelCommand(), new HomeNameCompleter());

        registerCommand("tpa", new TpaCommand(), new PlayerNameCompleter());
        registerCommand("tpaHere", new TpaCommand(), new PlayerNameCompleter());
        registerCommand("tpAccept", new TpHandleCommand(), new TpRequestCompleter());
        registerCommand("tpDeny", new TpHandleCommand(), new TpRequestCompleter());

        registerCommand("listWarps", new WarpListCommand());
        registerCommand("warpInfo", new WarpInfoCommand(), new WarpNameCompleter(false));
        registerCommand("warp", new WarpTpCommand(), new WarpNameCompleter(false));
        registerCommand("setWarp", new WarpSetCommand());
        registerCommand("delWarp", new WarpDelCommand(), new WarpNameCompleter(true));

        if (PluginConfig.METRICS.get()) {
            info("启用统计数据...");
            Metrics metrics = new Metrics(this, 14459);
            metrics.addCustomChart(new SimplePie("storage_method", storageMethod::name));
        }

        if (PluginConfig.CHECK_UPDATE.get()) {
            info("开始检查更新...");
            UpdateChecker.checkUpdate();
        } else {
            info("已禁用检查更新，跳过。");
        }

        return true;
    }

    @Override
    protected void shutdown() {
        info("关闭所有请求...");
        this.requestManager.shutdown();

        info("保存用户数据...");
        this.userManager.unloadAll(true);

        info("保存地标数据...");
        this.warpManager.saveWarps();

        info("终止存储源...");
        this.storage.shutdown();

        info("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);
    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.get();
    }

    public void outputInfo() {
        String[] pluginInfo = JarResourceUtils.readResource(this.getResource("PLUGIN_INFO"));
        if (pluginInfo != null) {
            Arrays.stream(pluginInfo).forEach(Main::info);
        }
    }


}
