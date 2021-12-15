package cc.carm.plugin.moeteleport.user;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

public class UserData {

	private final @NotNull File dataFile;
	private final @NotNull FileConfiguration dataConfig;

	private LinkedHashMap<String, DataLocation> homeLocations;

	public UserData(@NotNull File dataFolder, @NotNull UUID uuid) {
		this(new File(dataFolder, uuid + ".yml"));
	}

	public UserData(@NotNull File file) {
		if (!file.exists()) {
			try {
				boolean success = file.createNewFile();
			} catch (IOException e) {
				Main.error("在加载用户 " + file.getName() + " 的数据时出现异常。");
				Main.error(e.getLocalizedMessage());
			}
		}
		this.dataFile = file;
		this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
		loadHomeData();
	}

	public void loadHomeData() {
		LinkedHashMap<String, DataLocation> data = new LinkedHashMap<>();
		Optional.ofNullable(getDataConfig().getConfigurationSection("homes"))
				.ifPresent(section -> section.getKeys(false).forEach(homeName -> {
					DataLocation location = DataLocation.deserializeText(section.getString(homeName));
					if (location != null) data.put(homeName, location);
				}));
		this.homeLocations = data;
	}

	public LinkedHashMap<String, DataLocation> getHomeLocations() {
		return homeLocations;
	}

	public @NotNull File getDataFile() {
		return dataFile;
	}

	public @NotNull FileConfiguration getDataConfig() {
		return dataConfig;
	}


	public LinkedHashMap<String, String> saveToMap() {
		LinkedHashMap<String, DataLocation> homeLocations = getHomeLocations();
		LinkedHashMap<String, String> data = new LinkedHashMap<>();
		if (homeLocations.isEmpty()) return data;
		homeLocations.forEach((name, loc) -> data.put(name, loc.serializeToText()));
		return data;
	}


	public void save() {
		try {
			getDataConfig().createSection("homes", saveToMap());
			getDataConfig().save(getDataFile());
		} catch (Exception ex) {
			Main.error("在保存 " + getDataFile().getName() + " 时出现异常。");
			Main.error(ex.getLocalizedMessage());
		}
	}
}
