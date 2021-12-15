package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.configuration.file.FileConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigManager {

	private static FileConfig config;
	private static FileConfig messageConfig;

	private static HashMap<String, Integer> permissionsMap;

	public static void initConfig() {
		ConfigManager.config = new FileConfig(Main.getInstance(), "config.yml");
		ConfigManager.messageConfig = new FileConfig(Main.getInstance(), "messages.yml");
		permissionsMap = loadPermissions();
	}

	public static FileConfig getPluginConfig() {
		return config;
	}

	public static FileConfig getMessageConfig() {
		return messageConfig;
	}

	public static HashMap<String, Integer> getPermissions() {
		return permissionsMap;
	}

	public static HashMap<String, Integer> loadPermissions() {
		FileConfiguration config = getPluginConfig().getConfig();
		ConfigurationSection section = config.getConfigurationSection("permissions");
		if (section == null) return new HashMap<>();
		Set<String> permissionNodes = section.getKeys(false);
		if (permissionNodes.isEmpty()) return new HashMap<>();
		return permissionNodes.stream()
				.collect(Collectors.toMap(
						s -> s, s -> section.getInt(s, PluginConfig.DEFAULT_HOME.get()),
						(a, b) -> b, HashMap::new
				));
	}

	public static void reload() {
		getPluginConfig().reload();
		getMessageConfig().reload();
		permissionsMap = loadPermissions();
	}

	public static void saveConfig() {
		getPluginConfig().save();
		getMessageConfig().save();
	}


}
