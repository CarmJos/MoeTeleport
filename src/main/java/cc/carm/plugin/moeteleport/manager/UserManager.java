package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginConfig;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import cc.carm.plugin.moeteleport.util.DataTaskRunner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private final HashMap<UUID, UserData> userDataMap;

    public UserManager() {
        this.userDataMap = new HashMap<>();
    }

    @NotNull
    public UserData readData(UUID userUUID) {
        try {
            long start = System.currentTimeMillis();
            DataStorage storage = Main.getStorage();
            Main.debug("正通过 " + storage.getClass().getSimpleName() + " 读取 " + userUUID + " 的用户数据...(" + System.currentTimeMillis() + ")");

            UserData data = storage.loadData(userUUID);

            if (data == null) {
                Main.debug("当前还不存在玩家 " + userUUID + " 的数据，视作新档。");
                return new UserData(userUUID);
            }

            Main.debug("通过 " + storage.getClass().getSimpleName() + "读取 " + userUUID + " 的用户数据完成，"
                    + "耗时 " + (System.currentTimeMillis() - start) + "ms。");

            return data;
        } catch (Exception e) {
            Main.error("无法正常读取玩家数据，玩家操作将不会被保存，请检查数据配置！");
            Main.error("Could not load user's data, please check the data configuration!");
            e.printStackTrace();
            return new UserData(userUUID);
        }
    }

    public void saveData(UserData data) {
        try {
            long start = System.currentTimeMillis();
            DataStorage storage = Main.getStorage();

            Main.debug("正通过 " + storage.getClass().getSimpleName() + " 保存 " + data.getUserUUID() + " 的用户数据...(" + System.currentTimeMillis() + ")");
            storage.saveUserData(data);

            Main.debug("通过 " + storage.getClass().getSimpleName() + " 保存 " + data.getUserUUID() + " 的用户数据完成，" +
                    "耗时 " + (System.currentTimeMillis() - start) + "ms。");

        } catch (Exception e) {
            Main.error("无法正常保存玩家数据，请检查数据配置！");
            Main.error("Could not save user's data, please check the data configuration!");
            e.printStackTrace();
        }
    }

    public void loadData(UUID userUUID) {
        getUserDataMap().put(userUUID, readData(userUUID));
    }

    public void unloadData(UUID userUUID) {
        unloadData(userUUID, true);
    }

    public void unloadData(UUID userUUID, boolean save) {
        UserData data = getData(userUUID);
        if (data == null) return;
        if (save) saveData(data);
        getUserDataMap().remove(userUUID);
    }

    public void loadAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getUserDataMap().containsKey(player.getUniqueId())) continue;
            loadData(player.getUniqueId());
        }
    }

    public void saveAll() {
        getUserDataMap().values().forEach(this::saveData);
    }

    public void unloadAll(boolean save) {
        if (save) saveAll();
        getUserDataMap().clear();
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
        Map<Integer, String> permissions = PluginConfig.PERMISSIONS.get();
        int current = PluginConfig.DEFAULT_HOME.get();

        for (Map.Entry<Integer, String> entry : permissions.entrySet()) {
            if (entry.getKey() > current && player.hasPermission(entry.getValue())) {
                current = entry.getKey();
            }
        }
        return current;
    }

    public void editData(@NotNull DataTaskRunner task) {
        try {
            task.run(Main.getStorage());
        } catch (Exception exception) {
            Main.error("无法正常更改玩家数据，请检查数据配置！");
            Main.error("Could not edit user's data, please check the data configuration!");
            exception.printStackTrace();
        }
    }

    public void editDataAsync(@NotNull DataTaskRunner task) {
        Main.getScheduler().runAsync(() -> editData(task));
    }

    public HashMap<UUID, UserData> getUserDataMap() {
        return userDataMap;
    }

}
