package cc.carm.plugin.moeteleport;

import cc.carm.plugin.moeteleport.manager.RequestManager;
import cc.carm.plugin.moeteleport.manager.TeleportManager;
import cc.carm.plugin.moeteleport.manager.UserManager;
import cc.carm.plugin.moeteleport.manager.WarpManager;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import cc.carm.plugin.moeteleport.storage.StorageMethod;

public class MoeTeleport {

    public static DataStorage getStorage() {
        return Main.getInstance().storage;
    }

    public static WarpManager getWarpManager() {
        return Main.getInstance().warpManager;
    }

    public static UserManager getUserManager() {
        return Main.getInstance().userManager;
    }

    public static RequestManager getRequestManager() {
        return Main.getInstance().requestManager;
    }

    public static TeleportManager getTeleportManager() {
        return Main.getInstance().teleportManager;
    }


    public void setStorage(DataStorage storage) {
        Main.getInstance().storage = storage;
    }

    public void registerCustomStorage(Class<? extends DataStorage> storageClazz) {
        StorageMethod.CUSTOM.setStorageClazz(storageClazz);
    }

}
