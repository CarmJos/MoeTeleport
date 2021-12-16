package cc.carm.plugin.moeteleport.configuration;

import cc.carm.plugin.moeteleport.configuration.values.ConfigValue;
import cc.carm.plugin.moeteleport.configuration.values.ConfigValueList;
import cc.carm.plugin.moeteleport.configuration.values.ConfigValueMap;

public class PluginConfig {

	public static final ConfigValueMap<String, Integer> PERMISSIONS = new ConfigValueMap<>(
			"permissions", s -> s, Integer.class
	);

	public static final ConfigValueList<String> DANGEROUS_TYPES = new ConfigValueList<>(
			"dangerousBlocks", String.class, new String[]{"LAVA"}
	);

	public static final ConfigValue<Integer> EXPIRE_TIME = new ConfigValue<>(
			"expireTime", Integer.class, 30
	);
	public static final ConfigValue<Integer> DEFAULT_HOME = new ConfigValue<>(
			"defaultHome", Integer.class, 1
	);

}
