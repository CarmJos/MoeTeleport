package cc.carm.plugin.moeteleport.storage;

import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import cc.carm.plugin.moeteleport.manager.UserManager;
import cc.carm.plugin.moeteleport.model.UserData;
import cc.carm.plugin.moeteleport.model.WarpInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public interface DataStorage {

    /**
     * 在插件加载存储源时执行。
     *
     * @return 是否初始化成功
     */
    boolean initialize();

    /**
     * 在插件被卸载时执行。
     */
    void shutdown();

    /**
     * 用于加载用户数据的方法。<b>该方法将会被异步运行！</b>
     * <br>该方法一般无需自行执行，见 {@link UserManager#loadData(UUID)}
     * <br>
     * <br>若不存在该用户的数据，请返回 null 。
     * <br>若加载出现任何错误，请抛出异常。
     *
     * @param uuid 用户UUID
     * @throws Exception 当出现任何错误时抛出
     */
    @Nullable
    UserData loadData(@NotNull UUID uuid) throws Exception;

    /**
     * 用于保存用户数据的方法。 <b>该方法将会被异步运行！</b>
     * <br>该方法一般无需自行执行，见 {@link UserManager#saveData(UserData)}
     *
     * @param data 用户数据
     * @throws Exception 当出现任何错误时抛出
     */
    void saveUserData(@NotNull UserData data) throws Exception;

    Map<String, WarpInfo> getWarps();

    default void saveWarps() throws Exception {
        saveWarps(getWarps());
    }

    void saveWarps(@NotNull Map<String, WarpInfo> warps) throws Exception;

    void setWarp(@NotNull String name, @NotNull WarpInfo warpInfo) throws Exception;

    boolean delWarp(@NotNull String name) throws Exception;

    default boolean hasWarp(@NotNull String name) {
        return getWarps().containsKey(name);
    }

    /**
     * 为某用户设定一个家的位置。
     *
     * @param uuid         用户UUID
     * @param homeName     家的名称
     * @param homeLocation 家的位置
     * @throws Exception 当出现任何错误时抛出
     */
    void setHome(@NotNull UUID uuid, @NotNull String homeName, @NotNull DataLocation homeLocation) throws Exception;


    /**
     * 为某用户移除一个家的位置。
     *
     * @param uuid     用户UUID
     * @param homeName 家的名称
     * @return 是否有一个家被移除
     * @throws Exception 当出现任何错误时抛出
     */
    boolean delHome(@NotNull UUID uuid, @NotNull String homeName) throws Exception;

}
