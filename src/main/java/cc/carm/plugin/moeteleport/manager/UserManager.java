package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.model.UserData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

	private final File dataFolder;

	private final HashMap<UUID, UserData> userDataMap = new HashMap<>();

	public UserManager(Main main) {
		this.dataFolder = new File(main.getDataFolder() + "/data");
		if (!dataFolder.isDirectory() || !dataFolder.exists()) {
			boolean success = dataFolder.mkdir();
		}
	}

	@NotNull
	public UserData loadData(UUID userUUID) {
		return new UserData(getDataFolder(), userUUID);
	}

	@Nullable
	public UserData getData(UUID userUUID) {
		return getUserDataMap().get(userUUID);
	}

	@NotNull
	public UserData getData(Player player) {
		return getUserDataMap().get(player.getUniqueId());
	}

	public int getMaxHome(Player player) {
		Map<String, Integer> permissions = PluginConfig.PERMISSIONS.get();
		int value = PluginConfig.DEFAULT_HOME.get();
		for (Map.Entry<String, Integer> entry : permissions.entrySet()) {
			if (entry.getValue() > value && player.hasPermission(
					Main.getInstance().getName() + "." + entry.getKey()
			)) {
				value = entry.getValue();
			}
		}
		return value;
	}

	public HashMap<UUID, UserData> getUserDataMap() {
		return userDataMap;
	}

	public File getDataFolder() {
		return dataFolder;
	}
}
