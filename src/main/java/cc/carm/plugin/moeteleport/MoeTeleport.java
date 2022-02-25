package cc.carm.plugin.moeteleport;

import cc.carm.plugin.moeteleport.manager.RequestManager;
import cc.carm.plugin.moeteleport.manager.UserManager;
import cc.carm.plugin.moeteleport.manager.WarpManager;
import cc.carm.plugin.moeteleport.storage.DataStorage;
import cc.carm.plugin.moeteleport.storage.StorageMethod;

import java.util.function.Supplier;

public class MoeTeleport {

    public static void outputInfo() {
        Main.getInstance().outputInfo();
    }

    public static DataStorage getStorage() {
        return Main.getInstance().getStorage();
    }

    public static WarpManager getWarpManager() {
        return Main.getInstance().getWarpManager();
    }

    public static UserManager getUserManager() {
        return Main.getInstance().getUserManager();
    }

    public static RequestManager getRequestManager() {
        return Main.getInstance().getRequestManager();
    }

    public void registerCustomStorage(DataStorage storage) {
        registerCustomStorage(() -> storage);
    }

    public void registerCustomStorage(Supplier<DataStorage> storageSupplier) {
        StorageMethod.CUSTOM.setStorageSupplier(storageSupplier);
    }

}
