package cc.carm.plugin.moeteleport.storage.database;

import cc.carm.plugin.moeteleport.configuration.values.ConfigValue;

public class DBTables {

    protected static class UserLastLocations {

        protected static final ConfigValue<String> TABLE_NAME = new ConfigValue<>(
                "storage.mysql.tables.last-location", String.class,
                "mt_user_last_locations"
        );

        protected static final String[] TABLE_COLUMNS = new String[]{
                "`uuid` VARCHAR(36) NOT NULL PRIMARY KEY", // 用户的UUID
                "`world` VARCHAR(128) NOT NULL",// 最后地址的所在世界
                "`x` DOUBLE NOT NULL",// 最后地址的X坐标
                "`y` DOUBLE NOT NULL",// 最后地址的Y坐标
                "`z` DOUBLE NOT NULL",// 最后地址的Z坐标
                "`yaw` DOUBLE NOT NULL",// 最后地址的Yaw角度
                "`pitch` DOUBLE NOT NULL",// 最后地址的Pitch角度
                "`update` DATETIME NOT NULL " +
                        "DEFAULT CURRENT_TIMESTAMP " +
                        "ON UPDATE CURRENT_TIMESTAMP "
        };

    }

    protected static class UserHomes {

        protected static final ConfigValue<String> TABLE_NAME = new ConfigValue<>(
                "storage.mysql.tables.home", String.class,
                "mt_user_homes"
        );

        protected static final String[] TABLE_COLUMNS = new String[]{
                "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '背包ID'",
                "`uuid` VARCHAR(36) NOT NULL", // 用户的UUID
                "`name` VARCHAR(32) NOT NULL",
                "`world` VARCHAR(128) NOT NULL",
                "`x` DOUBLE NOT NULL",
                "`y` DOUBLE NOT NULL",
                "`z` DOUBLE NOT NULL",
                "`yaw` DOUBLE NOT NULL",
                "`pitch` DOUBLE NOT NULL",
                "INDEX `user`(`uuid`)",
                "UNIQUE KEY `home`(`uuid`,`name`)"
        };

    }

}