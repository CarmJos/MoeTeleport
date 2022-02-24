package cc.carm.plugin.moeteleport.storage.file;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
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
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

public class JSONStorage extends FileBasedStorage {

    protected static final Gson GSON = new Gson();
    protected static final JsonParser PARSER = new JsonParser();

    @Override
    public @Nullable UserData loadData(@NotNull UUID uuid) throws Exception {
        File userDataFile = new File(getDataFolder(), uuid + ".json");
        if (!userDataFile.exists()) {
            Main.debug("当前文件夾内不存在玩家 " + uuid + " 的数据，视作新档。");
            return null;
        }

        JsonElement dataElement = PARSER.parse(new FileReader(userDataFile));
        if (!dataElement.isJsonObject()) throw new NullPointerException(userDataFile.getName());

        JsonObject dataObject = dataElement.getAsJsonObject();

        DataLocation lastLocation = Optional
                .ofNullable(dataObject.get("lastLocation").getAsString())
                .map(DataLocation::deserializeText)
                .orElse(null);

        LinkedHashMap<String, DataLocation> homeData = new LinkedHashMap<>();
        JsonObject homesObject = dataObject.getAsJsonObject("homes");
        if (homesObject != null) {
            homesObject.entrySet().forEach(entry -> {
                DataLocation location = DataLocation.deserializeText(entry.getValue().getAsString());
                if (location != null) homeData.put(entry.getKey(), location);
            });
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

}
