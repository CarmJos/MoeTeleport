package cc.carm.plugin.moeteleport.storage.database;

import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLTable;
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import cc.carm.lib.easysql.api.enums.IndexType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.function.Consumer;

public enum DatabaseTables implements SQLTable {

    LAST_LOCATION(DatabaseConfig.TABLES.LAST_LOCATION, (table) -> {
        table.addColumn("uuid", "CHAR(36) NOT NULL PRIMARY KEY"); // 用户的UUID

        // 坐标世界与位置
        table.addColumn("world", "VARCHAR(128) NOT NULL");
        table.addColumn("x", "DOUBLE NOT NULL");
        table.addColumn("y", "DOUBLE NOT NULL");
        table.addColumn("z", "DOUBLE NOT NULL");
        table.addColumn("yaw", "DOUBLE NOT NULL");
        table.addColumn("pitch", "DOUBLE NOT NULL");

        table.addColumn("update",
                "DATETIME NOT NULL " +
                        "DEFAULT CURRENT_TIMESTAMP " +
                        "ON UPDATE CURRENT_TIMESTAMP"
        );
    }),

    HOMES(DatabaseConfig.TABLES.HOMES, (table) -> {
        table.addAutoIncrementColumn("id", true, true);
        table.addColumn("uuid", "CHAR(36) NOT NULL");
        table.addColumn("name", "VARCHAR(32) NOT NULL");

        // 坐标世界与位置
        table.addColumn("world", "VARCHAR(128) NOT NULL");
        table.addColumn("x", "DOUBLE NOT NULL");
        table.addColumn("y", "DOUBLE NOT NULL");
        table.addColumn("z", "DOUBLE NOT NULL");
        table.addColumn("yaw", "DOUBLE NOT NULL");
        table.addColumn("pitch", "DOUBLE NOT NULL");

        table.setIndex(IndexType.INDEX, "idx_homes_user", "uuid");
        table.setIndex(IndexType.UNIQUE_KEY, "uk_homes", "uuid", "name");
    }),

    WRAPS(DatabaseConfig.TABLES.WARPS, (table) -> {

        table.addAutoIncrementColumn("id", true, true);
        table.addColumn("name", "VARCHAR(32) NOT NULL");
        table.addColumn("owner", "CHAR(36) NOT NULL");

        // 坐标世界与位置
        table.addColumn("world", "VARCHAR(128) NOT NULL");
        table.addColumn("x", "DOUBLE NOT NULL");
        table.addColumn("y", "DOUBLE NOT NULL");
        table.addColumn("z", "DOUBLE NOT NULL");
        table.addColumn("yaw", "DOUBLE NOT NULL");
        table.addColumn("pitch", "DOUBLE NOT NULL");

        table.setIndex(IndexType.UNIQUE_KEY, "uk_wraps", "name");
    });

    private final Consumer<TableCreateBuilder> builder;
    private final ConfigValue<String> name;
    private @Nullable SQLManager manager;

    DatabaseTables(ConfigValue<String> name,
                   Consumer<TableCreateBuilder> builder) {
        this.name = name;
        this.builder = builder;
    }

    @Override
    public @Nullable SQLManager getSQLManager() {
        return this.manager;
    }

    @Override
    public @NotNull String getTableName() {
        return this.name.getNotNull();
    }

    @Override
    public boolean create(SQLManager sqlManager) throws SQLException {
        if (this.manager == null) this.manager = sqlManager;

        TableCreateBuilder tableBuilder = sqlManager.createTable(getTableName());
        if (builder != null) builder.accept(tableBuilder);
        return tableBuilder.build().executeFunction(l -> l > 0, false);
    }
}