package cc.carm.plugin.moeteleport.storage.database;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.util.UUIDUtil;
import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.conf.location.DataLocation;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import cc.carm.plugin.moeteleport.storage.UserData;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class MySQLStorage implements DataStorage {

    SQLManager sqlManager;

    Map<String, WarpInfo> warpsMap = new HashMap<>();

    @Override
    public void initialize() throws Exception {

        Main.info("加载数据库配置...");
        Main.getInstance().getConfigProvider().initialize(DatabaseConfig.class);

        try {
            Main.info("	尝试连接到数据库...");
            this.sqlManager = EasySQL.createManager(
                    DatabaseConfig.DRIVER_NAME.getNotNull(), DatabaseConfig.buildJDBC(),
                    DatabaseConfig.USERNAME.getNotNull(), DatabaseConfig.PASSWORD.getNotNull()
            );
            this.sqlManager.setDebugMode(() -> Main.getInstance().isDebugging());
        } catch (Exception exception) {
            throw new Exception("无法连接到数据库，请检查配置文件", exception);
        }

        try {
            Main.info("	创建插件所需表...");
            for (DatabaseTables value : DatabaseTables.values()) {
                value.create(this.sqlManager);
            }
        } catch (SQLException exception) {
            throw new Exception("无法创建插件所需的表，请检查数据库权限。", exception);
        }

        Main.info("	加载地标数据...");
        try {
            this.warpsMap = loadWarps();
        } catch (Exception e) {
            throw new Exception("无法加载地标数据，请检查数据库权限和相关表。", e);
        }
    }

    @Override
    public void shutdown() {
        Main.info("	关闭数据库连接...");
        EasySQL.shutdownManager(getSQLManager());
        this.sqlManager = null;
    }

    public SQLManager getSQLManager() {
        return sqlManager;
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
            DatabaseTables.LAST_LOCATION.createReplace()
                    .setColumnNames("uuid", "world", "x", "y", "z", "yaw", "pitch")
                    .setParams(
                            data.getUserUUID(), location.getWorld().getName(),
                            location.getX(), location.getY(), location.getZ(),
                            location.getYaw(), location.getPitch()
                    ).execute();
        } else {
            DatabaseTables.LAST_LOCATION.createDelete()
                    .addCondition("uuid", data.getUserUUID()).setLimit(1)
                    .build().execute();
        }
    }

    @Override
    public Map<String, WarpInfo> getWarps() {
        return this.warpsMap;
    }

    @Override
    public void saveWarps(@NotNull Map<String, WarpInfo> warps) {
        // 单独保存，不需要统一存储
    }

    private @NotNull LinkedHashMap<String, DataLocation> loadHomes(@NotNull UUID uuid) throws Exception {
        return DatabaseTables.HOMES.createQuery()
                .addCondition("uuid", uuid).build()
                .executeFunction((query) -> {
                    LinkedHashMap<String, DataLocation> homes = new LinkedHashMap<>();
                    ResultSet resultSet = query.getResultSet();
                    if (resultSet == null) return homes;
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        if (name == null) continue;
                        homes.put(name, readLocation(resultSet));
                    }
                    return homes;
                }, new LinkedHashMap<>());
    }

    private @Nullable DataLocation loadLastLocation(@NotNull UUID uuid) throws Exception {
        return DatabaseTables.LAST_LOCATION.createQuery()
                .addCondition("uuid", uuid).setLimit(1).build()
                .executeFunction((query) -> {
                    ResultSet resultSet = query.getResultSet();
                    if (resultSet == null || !resultSet.next()) return null;
                    return readLocation(resultSet);
                });
    }

    private @NotNull Map<String, WarpInfo> loadWarps() throws Exception {
        return DatabaseTables.WRAPS.createQuery()
                .orderBy("id", true).build().executeFunction((query) -> {
                    LinkedHashMap<String, WarpInfo> warps = new LinkedHashMap<>();
                    ResultSet resultSet = query.getResultSet();
                    if (resultSet == null) return warps;
                    while (resultSet.next()) {
                        String uuidString = resultSet.getString("owner");
                        UUID uuid = uuidString == null ? null : UUIDUtil.toUUID(uuidString);
                        String name = resultSet.getString("name");
                        DataLocation location = readLocation(resultSet);
                        warps.put(name, new WarpInfo(name, uuid, location));
                    }
                    return warps;
                }, new LinkedHashMap<>());
    }

    @Override
    public void setHome(@NotNull UUID uuid, @NotNull String homeName, @NotNull DataLocation location) throws Exception {
        DatabaseTables.HOMES.createReplace()
                .setColumnNames("uuid", "name", "world", "x", "y", "z", "yaw", "pitch")
                .setParams(
                        uuid, homeName, location.getWorldName(),
                        location.getX(), location.getY(), location.getZ(),
                        location.getYaw(), location.getPitch()
                ).execute();
    }


    @Override
    public boolean delHome(@NotNull UUID uuid, @NotNull String homeName) throws Exception {
        return DatabaseTables.HOMES.createDelete()
                .addCondition("uuid", uuid)
                .addCondition("name", homeName)
                .setLimit(1)
                .build().executeFunction((i) -> i > 0, false);
    }

    @Override
    public void setWarp(@NotNull String name, @NotNull WarpInfo warpInfo) throws Exception {
        this.warpsMap.put(name, warpInfo);
        DataLocation location = warpInfo.getLocation();
        DatabaseTables.WRAPS.createReplace()
                .setColumnNames("name", "owner", "world", "x", "y", "z", "yaw", "pitch")
                .setParams(
                        name, warpInfo.getOwner(), location.getWorldName(),
                        location.getX(), location.getY(), location.getZ(),
                        location.getYaw(), location.getPitch()
                ).execute();
    }

    @Override
    public boolean delWarp(@NotNull String name) throws Exception {
        String actualName = this.warpsMap.keySet().stream().filter(s -> s.equalsIgnoreCase(name)).findFirst().orElse(null);
        if (actualName == null) return false;

        this.warpsMap.remove(actualName);
        return DatabaseTables.WRAPS.createDelete()
                .addCondition("name", actualName).setLimit(1)
                .build().executeFunction((i) -> i > 0, false);
    }

    protected DataLocation readLocation(ResultSet result) throws SQLException {
        return new DataLocation(
                result.getString("world"),
                result.getDouble("x"),
                result.getDouble("y"),
                result.getDouble("z"),
                result.getFloat("yaw"),
                result.getFloat("pitch")
        );
    }


}
