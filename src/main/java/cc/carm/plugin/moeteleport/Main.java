package cc.carm.plugin.moeteleport;

import cc.carm.plugin.moeteleport.listener.UserListener;
import cc.carm.plugin.moeteleport.util.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main extends JavaPlugin {

	private static Main instance;
	public static boolean debugMode = true;

	@Override
	public void onEnable() {
		instance = this;
		log(getName() + " " + getDescription().getVersion() + " &7开始加载...");
		long startTime = System.currentTimeMillis();

		log("加载配置文件...");
		saveDefaultConfig();
		reloadConfig();

		log("注册监听器...");
		regListener(new UserListener());

		log("加载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

	}

	@Override
	public void onDisable() {
		log(getName() + " " + getDescription().getVersion() + " 开始卸载...");
		long startTime = System.currentTimeMillis();

		log("卸载监听器...");
		Bukkit.getServicesManager().unregisterAll(this);

		log("卸载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");
	}

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

	public static void debug(String message) {
		if (debugMode) {
			log("[DEBUG] " + message);
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

}
