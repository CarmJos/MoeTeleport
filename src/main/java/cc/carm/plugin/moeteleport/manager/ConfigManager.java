package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.configuration.file.FileConfig;

public class ConfigManager {

	private static FileConfig config;
	private static FileConfig messageConfig;

	public static void initConfig() {
		ConfigManager.config = new FileConfig(Main.getInstance(), "config.yml");
		ConfigManager.messageConfig = new FileConfig(Main.getInstance(), "messages.yml");
	}

	public static FileConfig getPluginConfig() {
		return config;
	}

	public static FileConfig getMessageConfig() {
		return messageConfig;
	}

	public static void reload() {
		getPluginConfig().reload();
		getMessageConfig().reload();
		PluginConfig.PERMISSIONS.clearCache();
	}

	public static void saveConfig() {
		getPluginConfig().save();
		getMessageConfig().save();
	}


}
