package cc.carm.plugin.moeteleport.storage;

import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import org.bukkit.Location;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataSerializer {

    public static Map<String, String> serializeLocationsMap(LinkedHashMap<String, DataLocation> data) {
        LinkedHashMap<String, String> after = new LinkedHashMap<>();
        if (data == null || data.isEmpty()) return after;
        data.forEach((name, loc) -> after.put(name, loc.serializeToText()));
        return after;
    }

    public static String serializeLocation(DataLocation loc) {
        return loc.serializeToText();
    }

    public static String serializeLocation(Location loc) {
        return serializeLocation(new DataLocation(loc));
    }


}
