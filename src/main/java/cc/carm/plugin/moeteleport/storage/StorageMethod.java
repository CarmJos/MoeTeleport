package cc.carm.plugin.moeteleport.storage;

import cc.carm.plugin.moeteleport.storage.impl.CustomStorage;
import cc.carm.plugin.moeteleport.storage.file.JSONStorage;
import cc.carm.plugin.moeteleport.storage.database.MySQLStorage;
import cc.carm.plugin.moeteleport.storage.file.YAMLStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public enum StorageMethod {
	
	CUSTOM(0, CustomStorage::new),
	YAML(1, YAMLStorage::new),
	JSON(2, JSONStorage::new),
	MYSQL(3, MySQLStorage::new);

	private final int id;
	private @NotNull Supplier<@NotNull DataStorage> storageSupplier;

	StorageMethod(int id, @NotNull Supplier<@NotNull DataStorage> storageSupplier) {
		this.id = id;
		this.storageSupplier = storageSupplier;
	}

	public int getID() {
		return id;
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
		try {
			return Optional.ofNullable(readByID(Integer.parseInt(s))).orElse(YAML);
		} catch (Exception ex) {
			return YAML;
		}
	}


	public static @Nullable StorageMethod readByName(String name) {
		return Arrays.stream(values()).filter(value -> value.name().equalsIgnoreCase(name)).findFirst().orElse(null);
	}


	public static @Nullable StorageMethod readByID(int id) {
		return Arrays.stream(values()).filter(value -> value.getID() == id).findFirst().orElse(null);
	}
}
