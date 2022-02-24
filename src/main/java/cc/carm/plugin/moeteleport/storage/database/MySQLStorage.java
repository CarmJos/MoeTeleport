package cc.carm.plugin.moeteleport.storage.database;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.UUID;

public class MySQLStorage implements DataStorage {

    SQLManager sqlManager;

    @Override
    public boolean initialize() {
        try {
            Main.log("	尝试连接到数据库...");
            String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false",
                    DBConfiguration.HOST.get(), DBConfiguration.PORT.get(), DBConfiguration.DATABASE.get()
            );
            this.sqlManager = EasySQL.createManager(
                    DBConfiguration.DRIVER_NAME.get(), url,
                    DBConfiguration.USERNAME.get(), DBConfiguration.PASSWORD.get()
            );
        } catch (Exception exception) {
            Main.error("无法连接到数据库，请检查配置文件。");
            exception.printStackTrace();
            return false;
        }

        try {
            Main.log("	创建插件所需表...");
            getSQLManager().createTable(DBTables.UserLastLocations.TABLE_NAME.get())
                    .setColumns(DBTables.UserLastLocations.TABLE_COLUMNS)
                    .build().execute();

            getSQLManager().createTable(DBTables.UserHomes.TABLE_NAME.get())
                    .setColumns(DBTables.UserHomes.TABLE_COLUMNS)
                    .build().execute();

        } catch (SQLException exception) {
            Main.error("无法创建插件所需的表，请检查数据库权限。");
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void shutdown() {
        Main.log("	关闭数据库连接...");
        EasySQL.shutdownManager(getSQLManager());
        this.sqlManager = null;
    }

    @Override
    public @Nullable UserData loadData(@NotNull UUID uuid) throws Exception {
        LinkedHashMap<String, DataLocation> homes = loadHomes(uuid);
        DataLocation lastLocation = loadLastLocation(uuid);
        return new UserData(uuid, lastLocation, homes);
    }

    @Override
    public void saveUserData(@NotNull UserData data) throws Exception {
        Location location = data.getLastLocation();
        if (location != null && location.getWorld() != null) {
            getSQLManager().createReplace(DBTables.UserLastLocations.TABLE_NAME.get())
                    .setColumnNames("uuid", "world", "x", "y", "z", "yaw", "pitch")
                    .setParams(
                            data.getUserUUID(), location.getWorld().getName(),
                            location.getX(), location.getY(), location.getZ(),
                            location.getYaw(), location.getPitch()
                    ).execute();
        } else {
            getSQLManager().createDelete(DBTables.UserLastLocations.TABLE_NAME.get())
                    .addCondition("uuid", data.getUserUUID()).setLimit(1)
                    .build().execute();
        }
    }

    private @NotNull LinkedHashMap<String, DataLocation> loadHomes(@NotNull UUID uuid) throws Exception {
        return getSQLManager().createQuery().inTable(DBTables.UserHomes.TABLE_NAME.get())
                .addCondition("uuid", uuid).build()
                .executeFunction((query) -> {
                    LinkedHashMap<String, DataLocation> homes = new LinkedHashMap<>();
                    ResultSet resultSet = query.getResultSet();
                    if (resultSet == null) return homes;
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        if (name == null) continue;
                        homes.put(name, new DataLocation(
                                resultSet.getString("world"),
                                resultSet.getDouble("x"),
                                resultSet.getDouble("y"),
                                resultSet.getDouble("z"),
                                resultSet.getFloat("yaw"),
                                resultSet.getFloat("pitch")
                        ));
                    }
                    return homes;
                }, new LinkedHashMap<>());
    }

    private @Nullable DataLocation loadLastLocation(@NotNull UUID uuid) throws Exception {
        return getSQLManager().createQuery().inTable(DBTables.UserLastLocations.TABLE_NAME.get())
                .addCondition("uuid", uuid).setLimit(1).build()
                .executeFunction((query) -> {
                    ResultSet resultSet = query.getResultSet();
                    if (resultSet == null || !resultSet.next()) return null;
                    return new DataLocation(
                            resultSet.getString("world"),
                            resultSet.getDouble("x"),
                            resultSet.getDouble("y"),
                            resultSet.getDouble("z"),
                            resultSet.getFloat("yaw"),
                            resultSet.getFloat("pitch")
                    );
                });
    }

    @Override
    public void setHome(@NotNull UUID uuid, @NotNull String homeName, @NotNull DataLocation location) throws Exception {
        getSQLManager().createReplace(DBTables.UserHomes.TABLE_NAME.get())
                .setColumnNames("uuid", "name", "world", "x", "y", "z", "yaw", "pitch")
                .setParams(
                        uuid, homeName, location.getWorldName(),
                        location.getX(), location.getY(), location.getZ(),
                        location.getYaw(), location.getPitch()
                ).execute();
    }


    @Override
    public boolean delHome(@NotNull UUID uuid, @NotNull String homeName) throws Exception {
        return getSQLManager().createDelete(DBTables.UserHomes.TABLE_NAME.get())
                .addCondition("uuid", uuid)
                .addCondition("name", homeName)
                .setLimit(1)
                .build().executeFunction((i) -> i > 0, false);
    }

    public SQLManager getSQLManager() {
        return sqlManager;
    }


}
