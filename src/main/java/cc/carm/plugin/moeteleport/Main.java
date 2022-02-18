package cc.carm.plugin.moeteleport;

import cc.carm.plugin.moeteleport.command.BackCommand;
import cc.carm.plugin.moeteleport.command.completer.HomeNameCompleter;
import cc.carm.plugin.moeteleport.command.completer.PlayerNameCompleter;
import cc.carm.plugin.moeteleport.command.completer.TpRequestCompleter;
import cc.carm.plugin.moeteleport.command.home.DelHomeCommand;
import cc.carm.plugin.moeteleport.command.home.GoHomeCommand;
import cc.carm.plugin.moeteleport.command.home.ListHomeCommand;
import cc.carm.plugin.moeteleport.command.home.SetHomeCommand;
import cc.carm.plugin.moeteleport.command.tpa.TpHandleCommand;
import cc.carm.plugin.moeteleport.command.tpa.TpaCommand;
import cc.carm.plugin.moeteleport.listener.UserListener;
import cc.carm.plugin.moeteleport.manager.ConfigManager;
import cc.carm.plugin.moeteleport.manager.RequestManager;
import cc.carm.plugin.moeteleport.manager.UserManager;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.util.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main extends JavaPlugin {

    public static boolean debugMode = true;
    private static Main instance;
    private UserManager userManager;
    private RequestManager requestManager;

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    public static void regListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getInstance());
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("[" + getInstance().getName() + "] " + message));
    }

    public static void error(String message) {
        log("&4[ERROR] &r" + message);
    }

    public static void debug(String message) {
        if (debugMode) {
            log("&b[DEBUG] &r" + message);
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public static void registerCommand(String commandName,
                                       @NotNull CommandExecutor executor) {
        registerCommand(commandName, executor, null);
    }

    public static void registerCommand(String commandName,
                                       @NotNull CommandExecutor executor,
                                       @Nullable TabCompleter tabCompleter) {
        PluginCommand command = Bukkit.getPluginCommand(commandName);
        if (command == null) return;
        command.setExecutor(executor);
        if (tabCompleter != null) command.setTabCompleter(tabCompleter);
    }

    public static UserManager getUserManager() {
        return Main.getInstance().userManager;
    }

    public static RequestManager getRequestManager() {
        return Main.getInstance().requestManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        log(getName() + " " + getDescription().getVersion() + " &7开始加载...");
        long startTime = System.currentTimeMillis();

        log("加载配置文件...");
        ConfigManager.initConfig();

        log("加载用户管理器...");
        this.userManager = new UserManager(this);

        if (Bukkit.getOnlinePlayers().size() > 0) {
            log("   加载现有用户数据...");
            for (Player player : Bukkit.getOnlinePlayers()) {
                UserData data = Main.getUserManager().loadData(player.getUniqueId());
                Main.getUserManager().getUserDataMap().put(player.getUniqueId(), data);
            }
        }

        log("加载请求管理器...");
        this.requestManager = new RequestManager(this);

        log("注册监听器...");
        regListener(new UserListener());

        log("注册指令...");
        registerCommand("back", new BackCommand());

        registerCommand("home", new GoHomeCommand(), new HomeNameCompleter());
        registerCommand("delHome", new DelHomeCommand(), new HomeNameCompleter());
        registerCommand("setHome", new SetHomeCommand());
        registerCommand("listHome", new ListHomeCommand());

        registerCommand("tpa", new TpaCommand(), new PlayerNameCompleter());
        registerCommand("tpaHere", new TpaCommand(), new PlayerNameCompleter());
        registerCommand("tpAccept", new TpHandleCommand(), new TpRequestCompleter());
        registerCommand("tpDeny", new TpHandleCommand(), new TpRequestCompleter());

        log("加载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

    }

    @Override
    public void onDisable() {
        log(getName() + " " + getDescription().getVersion() + " 开始卸载...");
        long startTime = System.currentTimeMillis();

        log("关闭所有请求...");
        getRequestManager().shutdown();

        log("保存用户数据...");
        getUserManager().getUserDataMap().values().forEach(UserData::save);
        getUserManager().getUserDataMap().clear();

        log("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);

        log("卸载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");
    }

}
