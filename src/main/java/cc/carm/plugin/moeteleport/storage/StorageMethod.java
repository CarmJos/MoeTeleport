package cc.carm.plugin.moeteleport.storage;

import cc.carm.plugin.moeteleport.storage.custom.CustomStorage;
import cc.carm.plugin.moeteleport.storage.database.MySQLStorage;
import cc.carm.plugin.moeteleport.storage.extension.EssentialStorage;
import cc.carm.plugin.moeteleport.storage.file.JSONStorage;
import cc.carm.plugin.moeteleport.storage.file.YAMLStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

public enum StorageMethod {

    CUSTOM(0, new String[]{}, CustomStorage.class),
    YAML(1, new String[]{"yml"}, YAMLStorage.class),
    JSON(2, new String[]{}, JSONStorage.class),
    MYSQL(3, new String[]{"my-sql", "mariadb", "sql", "database"}, MySQLStorage.class),

    ESSENTIALS(11, new String[]{"essential", "ess", "EssentialsX", "essX"}, EssentialStorage.class);

    private final int id;
    private final String[] alias;
    private @NotNull Class<? extends DataStorage> storageClazz;

    StorageMethod(int id, String[] alias, @NotNull Class<? extends DataStorage> storageClazz) {
        this.id = id;
        this.alias = alias;
        this.storageClazz = storageClazz;
    }

    public static @NotNull StorageMethod read(String s) {
        StorageMethod byName = readByName(s);
        if (byName != null) return byName;
        StorageMethod byAlias = readByAlias(s);
        if (byAlias != null) return byAlias;
        try {
            return Optional.ofNullable(readByID(Integer.parseInt(s))).orElse(YAML);
        } catch (Exception ex) {
            return YAML;
        }
    }

    public static @Nullable StorageMethod readByName(String name) {
        return Arrays.stream(values()).filter(value -> value.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static @Nullable StorageMethod readByAlias(String name) {
        return Arrays.stream(values())
                .filter(value -> Arrays.stream(value.getAlias()).anyMatch(alias -> alias.equalsIgnoreCase(name)))
                .findFirst().orElse(null);
    }

    public static @Nullable StorageMethod readByID(int id) {
        return Arrays.stream(values()).filter(value -> value.getID() == id).findFirst().orElse(null);
    }

    public int getID() {
        return id;
    }

    public String[] getAlias() {
        return alias;
    }

    public @NotNull Class<? extends DataStorage> getStorageClazz() {
        return storageClazz;
    }

    public void setStorageClazz(@NotNull Class<? extends DataStorage> storageClazz) {
        this.storageClazz = storageClazz;
    }

    public @NotNull DataStorage createStorage() throws Exception {
        return getStorageClazz().newInstance();
    }
}
