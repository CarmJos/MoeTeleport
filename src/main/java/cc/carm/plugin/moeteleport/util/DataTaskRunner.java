package cc.carm.plugin.moeteleport.util;

import cc.carm.plugin.moeteleport.storage.DataStorage;

@FunctionalInterface
public interface DataTaskRunner {
    void run(DataStorage storage) throws Exception;
}
