package cc.carm.plugin.moeteleport.storage;

import cc.carm.plugin.moeteleport.configuration.location.DataLocation;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class DataSerializer {

    public static Map<String, String> serializeLocationsMap(LinkedHashMap<String, DataLocation> data) {
        LinkedHashMap<String, String> after = new LinkedHashMap<>();
        if (data == null || data.isEmpty()) return after;
        data.forEach((name, loc) -> after.put(name, loc.serializeToText()));
        return after;
    }

    public static @Nullable String serializeLocation(@Nullable DataLocation loc) {
        return Optional.ofNullable(loc).map(DataLocation::serializeToText).orElse(null);
    }

    public static @Nullable String serializeLocation(@Nullable Location loc) {
        return serializeLocation(Optional.ofNullable(loc).map(DataLocation::new).orElse(null));
    }


}
