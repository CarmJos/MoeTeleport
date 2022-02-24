package cc.carm.plugin.moeteleport.storage.database;

import cc.carm.plugin.moeteleport.configuration.values.ConfigValue;

public class DBConfiguration {

    protected static final ConfigValue<String> DRIVER_NAME = new ConfigValue<>(
            "storage.mysql.driver", String.class,
            "com.mysql.cj.jdbc.Driver"
    );

    protected static final ConfigValue<String> HOST = new ConfigValue<>(
            "storage.mysql.host", String.class,
            "127.0.0.1"
    );

    protected static final ConfigValue<Integer> PORT = new ConfigValue<>(
            "storage.mysql.port", Integer.class,
            3306
    );

    protected static final ConfigValue<String> DATABASE = new ConfigValue<>(
            "storage.mysql.database", String.class,
            "minecraft"
    );

    protected static final ConfigValue<String> USERNAME = new ConfigValue<>(
            "storage.mysql.username", String.class,
            "root"
    );

    protected static final ConfigValue<String> PASSWORD = new ConfigValue<>(
            "storage.mysql.password", String.class,
            "password"
    );

}
