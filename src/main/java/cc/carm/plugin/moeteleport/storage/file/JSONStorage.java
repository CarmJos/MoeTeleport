package cc.carm.plugin.moeteleport.storage.file;

import cc.carm.lib.easysql.api.util.UUIDUtil;
import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import cc.carm.plugin.moeteleport.storage.DataSerializer;
import cc.carm.plugin.moeteleport.storage.impl.FileBasedStorage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class JSONStorage extends FileBasedStorage {

    protected static final Gson GSON = new Gson();
    protected static final JsonParser PARSER = new JsonParser();

    Map<String, WarpInfo> warpsMap = new HashMap<>();

    @Override
    public boolean initialize() {
        if (super.initialize()) {
            try {
                this.warpsMap = loadWarps();
                return true;
            } catch (Exception e) {
                Main.severe("无法加载地标数据，请检查文件权限和相关配置。");
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public @Nullable UserData loadData(@NotNull UUID uuid) throws Exception {
        File userDataFile = new File(getDataFolder(), uuid + ".json");
        if (!userDataFile.exists()) {
            Main.debugging("当前文件夾内不存在玩家 " + uuid + " 的数据，视作新档。");
            return null;
        }

        JsonElement dataElement = PARSER.parse(new FileReader(userDataFile));
        if (!dataElement.isJsonObject()) throw new NullPointerException(userDataFile.getName());

        JsonObject dataObject = dataElement.getAsJsonObject();
        DataLocation lastLocation = null;
        if (dataObject.has("lastLocation")) {
            lastLocation = DataLocation.deserializeText(dataObject.get("lastLocation").getAsString());
        }

        LinkedHashMap<String, DataLocation> homeData = new LinkedHashMap<>();
        if (dataObject.has("homes")) {
            JsonObject homesObject = dataObject.getAsJsonObject("homes");
            if (homesObject != null) {
                homesObject.entrySet().forEach(entry -> {
                    DataLocation location = DataLocation.deserializeText(entry.getValue().getAsString());
                    if (location != null) homeData.put(entry.getKey(), location);
                });
            }
        }

        return new UserData(uuid, lastLocation, homeData);
    }

    @Override
    public void saveUserData(@NotNull UserData data) throws Exception {
        JsonObject dataObject = new JsonObject();
        if (data.getLastLocation() != null) {
            dataObject.addProperty("lastLocation", DataSerializer.serializeLocation(data.getLastLocation()));
        }
        dataObject.add("homes", GSON.toJsonTree(DataSerializer.serializeLocationsMap(data.getHomeLocations())));

        FileWriter writer = new FileWriter(new File(getDataFolder(), data.getUserUUID() + ".json"));
        writer.write(GSON.toJson(dataObject));
        writer.flush();
        writer.close();
    }

    private @NotNull Map<String, WarpInfo> loadWarps() throws Exception {
        File warpDataFile = new File(getDataFolder(), "warps.json");
        if (!warpDataFile.exists()) return new LinkedHashMap<>();

        JsonElement dataElement = PARSER.parse(new FileReader(warpDataFile));
        if (!dataElement.isJsonObject()) throw new NullPointerException(warpDataFile.getName());

        JsonObject dataObject = dataElement.getAsJsonObject();
        LinkedHashMap<String, WarpInfo> warps = new LinkedHashMap<>();
        dataObject.entrySet().forEach(entry -> {
            String warpName = entry.getKey();
            if (entry.getValue().isJsonObject()) {
                try {
                    JsonObject warpObject = entry.getValue().getAsJsonObject();
                    UUID owner = warpObject.has("owner") ? UUIDUtil.toUUID(warpObject.get("owner").getAsString()) : null;
                    DataLocation location = new DataLocation(
                            warpObject.get("world").getAsString(),
                            warpObject.get("x").getAsDouble(),
                            warpObject.get("y").getAsDouble(),
                            warpObject.get("z").getAsDouble(),
                            warpObject.get("yaw").getAsFloat(),
                            warpObject.get("pitch").getAsFloat()
                    );
                    warps.put(warpName, new WarpInfo(warpName, owner, location));
                } catch (Exception ignore) {
                }
            }
        });
        return warps;
    }

    @Override
    public void saveWarps(@NotNull Map<String, WarpInfo> warps) throws Exception {
        JsonObject dataObject = new JsonObject();

        warps.forEach((name, info) -> dataObject.add(name, GSON.toJsonTree(DataSerializer.serializeWarpMap(info))));

        FileWriter writer = new FileWriter(new File(getDataFolder(), "warps.json"));
        writer.write(GSON.toJson(dataObject));
        writer.flush();
        writer.close();
    }

    @Override
    public Map<String, WarpInfo> getWarps() {
        return warpsMap;
    }

    @Override
    public void setWarp(@NotNull String name, @NotNull WarpInfo warpInfo) {
        this.warpsMap.put(name, warpInfo);
    }

    @Override
    public boolean delWarp(@NotNull String name) {
        return this.warpsMap.remove(name) != null;
    }


}
