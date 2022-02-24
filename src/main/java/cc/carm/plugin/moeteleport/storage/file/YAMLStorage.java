package cc.carm.plugin.moeteleport.storage.file;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.storage.DataSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

public class YAMLStorage extends FileBasedStorage {

    @Override
    public @Nullable UserData loadData(@NotNull UUID uuid) {
        if (getDataFolder() == null || !getDataFolder().exists() || !getDataFolder().isDirectory()) {
            throw new NullPointerException("Storage data folder is not initialized.");
        }
        File userDataFile = new File(getDataFolder(), uuid + ".yml");
        if (!userDataFile.exists()) {
            Main.debug("当前文件夾内不存在玩家 " + uuid + " 的数据，视作新档。");
            return null;
        }

        YamlConfiguration userConfiguration = YamlConfiguration.loadConfiguration(userDataFile);

        DataLocation lastLocation = Optional
                .ofNullable(userConfiguration.getString("lastLocation"))
                .map(DataLocation::deserializeText)
                .orElse(null);

        LinkedHashMap<String, DataLocation> homeData = new LinkedHashMap<>();
        Optional.ofNullable(userConfiguration.getConfigurationSection("homes")).ifPresent(
                section -> section.getKeys(false).forEach(homeName -> {
                    DataLocation location = DataLocation.deserializeText(section.getString(homeName));
                    if (location != null) homeData.put(homeName, location);
                }));

        return new UserData(uuid, lastLocation, homeData);
    }

    @Override
    public void saveUserData(@NotNull UserData data) throws Exception {
        if (getDataFolder() == null || !getDataFolder().exists() || !getDataFolder().isDirectory()) {
            throw new NullPointerException("Storage data folder is not initialized.");
        }

        YamlConfiguration userConfiguration = new YamlConfiguration();
        if (data.getLastLocation() != null) {
            userConfiguration.set("lastLocation", DataSerializer.serializeLocation(data.getLastLocation()));
        }

        userConfiguration.createSection("homes", DataSerializer.serializeLocationsMap(data.getHomeLocations()));
        userConfiguration.save(new File(getDataFolder(), data.getUserUUID() + ".yml"));
    }

}
