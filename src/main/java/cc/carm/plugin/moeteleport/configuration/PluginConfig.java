package cc.carm.plugin.moeteleport.configuration;

import cc.carm.lib.easyplugin.configuration.values.ConfigValue;
import cc.carm.lib.easyplugin.configuration.values.ConfigValueList;
import cc.carm.lib.easyplugin.configuration.values.ConfigValueMap;

public class PluginConfig {

    public static final ConfigValue<Boolean> DEBUG = new ConfigValue<>(
            "debug", Boolean.class, false
    );

    public static final ConfigValue<Boolean> METRICS = new ConfigValue<>(
            "metrics", Boolean.class, true
    );

    public static final ConfigValue<Boolean> CHECK_UPDATE = new ConfigValue<>(
            "check-update", Boolean.class, true
    );

    public static final ConfigValue<String> STORAGE_METHOD = new ConfigValue<>(
            "storage.method", String.class, "YAML"
    );

    public static final ConfigValueMap<Integer, String> HOME_PERMISSIONS = new ConfigValueMap<>(
            "permissions.home", Integer::parseInt, String.class
    );

    public static final ConfigValueMap<Integer, String> WARP_PERMISSIONS = new ConfigValueMap<>(
            "permissions.warp", Integer::parseInt, String.class
    );

    public static final ConfigValueList<String> DANGEROUS_TYPES = new ConfigValueList<>(
            "dangerous-blocks", String.class, new String[]{"LAVA"}
    );

    public static final ConfigValue<Integer> EXPIRE_TIME = new ConfigValue<>(
            "expireTime", Integer.class, 30
    );

    public static final ConfigValue<Integer> DEFAULT_HOME = new ConfigValue<>(
            "defaults.home", Integer.class, 1
    );

    public static final ConfigValue<Integer> DEFAULT_WARP = new ConfigValue<>(
            "defaults.warp", Integer.class, 0
    );

    public static final ConfigValue<Boolean> DEATH_GO_BACK = new ConfigValue<>(
            "death-back", Boolean.class, true
    );

}
