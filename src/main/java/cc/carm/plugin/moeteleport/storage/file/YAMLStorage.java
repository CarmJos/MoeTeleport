package cc.carm.plugin.moeteleport.storage.file;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import cc.carm.plugin.moeteleport.storage.DataSerializer;
import cc.carm.plugin.moeteleport.storage.impl.FileBasedStorage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class YAMLStorage extends FileBasedStorage {

    Map<String, WarpInfo> warpsMap;

    File warpsDataFile;
    FileConfiguration warpsConfiguration;

    @Override
    public boolean initialize() {
        if (super.initialize()) {
            try {
                this.warpsDataFile = new File(getDataFolder(), "warps.yml");
                if (!this.warpsDataFile.exists()) {
                    boolean success = warpsDataFile.createNewFile();
                }
                this.warpsConfiguration = YamlConfiguration.loadConfiguration(warpsDataFile);
                this.warpsMap = loadWarps();
                return true;
            } catch (Exception e) {
                Main.serve("无法加载地标数据，请检查文件权限和相关配置。");
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void saveWarps(@NotNull Map<String, WarpInfo> warps) throws Exception {
        this.warpsConfiguration.save(this.warpsDataFile);
    }

    @Override
    public @Nullable UserData loadData(@NotNull UUID uuid) {
        if (getDataFolder() == null || !getDataFolder().exists() || !getDataFolder().isDirectory()) {
            throw new NullPointerException("Storage data folder is not initialized.");
        }
        File userDataFile = new File(getDataFolder(), uuid + ".yml");
        if (!userDataFile.exists()) {
            Main.debugging("当前文件夾内不存在玩家 " + uuid + " 的数据，视作新档。");
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

    private @NotNull Map<String, WarpInfo> loadWarps() {
        LinkedHashMap<String, WarpInfo> warps = new LinkedHashMap<>();

        ConfigurationSection warpsSection = this.warpsConfiguration.getConfigurationSection("warps");
        if (warpsSection == null) return warps;

        for (String warpName : warpsSection.getKeys(false)) {
            ConfigurationSection warpInfoSection = warpsSection.getConfigurationSection(warpName);
            if (warpInfoSection == null) continue;
            try {
                String ownerString = warpInfoSection.getString("owner");
                UUID owner = ownerString == null ? null : UUID.fromString(ownerString);
                DataLocation location = new DataLocation(
                        warpInfoSection.getString("world"),
                        warpInfoSection.getDouble("x"),
                        warpInfoSection.getDouble("y"),
                        warpInfoSection.getDouble("z"),
                        warpInfoSection.getLong("yaw"),
                        warpInfoSection.getLong("pitch")
                );
                warps.put(warpName, new WarpInfo(warpName, owner, location));
            } catch (Exception ignore) {
            }
        }

        return warps;
    }

    @Override
    public Map<String, WarpInfo> getWarps() {
        return warpsMap;
    }

    @Override
    public void setWarp(@NotNull String name, @NotNull WarpInfo warpInfo) {
        this.warpsMap.put(name, warpInfo);
        this.warpsConfiguration.createSection("warps." + name, DataSerializer.serializeWarpMap(warpInfo));
    }

    @Override
    public boolean delWarp(@NotNull String name) {
        this.warpsConfiguration.set("warps." + name, null);
        return this.warpsMap.remove(name) != null;
    }


}
