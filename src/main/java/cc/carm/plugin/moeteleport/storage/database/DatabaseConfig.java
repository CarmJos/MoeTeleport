package cc.carm.plugin.moeteleport.storage.database;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;


@HeaderComment("选择 database (如mysql) 存储方式时的数据库配置")
@ConfigPath("storage.database")
public class DatabaseConfig extends ConfigurationRoot {

    @ConfigPath("driver")
    protected static final ConfigValue<String> DRIVER_NAME = ConfiguredValue.of(
            String.class, "com.mysql.jdbc.Driver"
    );

    protected static final ConfigValue<String> HOST = ConfiguredValue.of(String.class, "127.0.0.1");
    protected static final ConfigValue<Integer> PORT = ConfiguredValue.of(Integer.class, 3306);
    protected static final ConfigValue<String> DATABASE = ConfiguredValue.of(String.class, "minecraft");
    protected static final ConfigValue<String> USERNAME = ConfiguredValue.of(String.class, "root");
    protected static final ConfigValue<String> PASSWORD = ConfiguredValue.of(String.class, "password");
    protected static final ConfigValue<String> EXTRA = ConfiguredValue.of(String.class, "?useSSL=false");

    @HeaderComment("插件相关表的名称")
    public static final class TABLES extends ConfigurationRoot {

        public static final ConfigValue<String> LAST_LOCATION = ConfiguredValue.of(String.class, "mt_last_locations");
        public static final ConfigValue<String> HOMES = ConfiguredValue.of(String.class, "mt_homes");
        public static final ConfigValue<String> WARPS = ConfiguredValue.of(String.class, "mt_warps");

    }

    protected static String buildJDBC() {
        return String.format("jdbc:mysql://%s:%s/%s%s",
                HOST.getNotNull(), PORT.getNotNull(), DATABASE.getNotNull(), EXTRA.getNotNull()
        );
    }

}