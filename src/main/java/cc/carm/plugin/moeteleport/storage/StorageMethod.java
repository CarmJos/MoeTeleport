package cc.carm.plugin.moeteleport.storage;

import cc.carm.plugin.moeteleport.storage.database.MySQLStorage;
import cc.carm.plugin.moeteleport.storage.extension.EssentialXStorage;
import cc.carm.plugin.moeteleport.storage.file.JSONStorage;
import cc.carm.plugin.moeteleport.storage.file.YAMLStorage;
import cc.carm.plugin.moeteleport.storage.custom.CustomStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public enum StorageMethod {

    CUSTOM(0, new String[]{}, CustomStorage::new),
    YAML(1, new String[]{"yml"}, YAMLStorage::new),
    JSON(2, new String[]{}, JSONStorage::new),
    MYSQL(3, new String[]{"my-sql", "mariadb", "sql", "database"}, MySQLStorage::new),

    ESSENTIALS(11, new String[]{"essential", "ess", "EssentialsX", "essX"}, EssentialXStorage::new);

    private final int id;
    private final String[] alias;
    private @NotNull Supplier<@NotNull DataStorage> storageSupplier;

    StorageMethod(int id, String[] alias, @NotNull Supplier<@NotNull DataStorage> storageSupplier) {
        this.id = id;
        this.alias = alias;
        this.storageSupplier = storageSupplier;
    }

    public int getID() {
        return id;
    }

    public String[] getAlias() {
        return alias;
    }

    public @NotNull Supplier<@NotNull DataStorage> getStorageSupplier() {
        return storageSupplier;
    }

    public void setStorageSupplier(@NotNull Supplier<@NotNull DataStorage> storageSupplier) {
        this.storageSupplier = storageSupplier;
    }

    public @NotNull DataStorage createStorage() {
        return getStorageSupplier().get();
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
}
