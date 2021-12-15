package cc.carm.plugin.moeteleport.configuration;

import cc.carm.plugin.moeteleport.configuration.values.ConfigValue;
import cc.carm.plugin.moeteleport.manager.ConfigManager;

import java.util.HashMap;

public class PluginConfig {

	public static HashMap<String, Integer> getPermissions() {
		return ConfigManager.getPermissions();
	}

	public static final ConfigValue<Integer> EXPIRE_TIME = new ConfigValue<>("expireTime", Integer.class, 30);
	public static final ConfigValue<Integer> DEFAULT_HOME = new ConfigValue<>("defaultHome", Integer.class, 1);

}
