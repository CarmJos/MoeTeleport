package cc.carm.plugin.moeteleport.configuration.values;

import cc.carm.plugin.moeteleport.configuration.file.FileConfig;
import cc.carm.plugin.moeteleport.manager.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class ConfigValueMap<K, V> {

    @NotNull FileConfig source;
    @NotNull String configSection;

    @NotNull Function<String, K> keyCast;
    @NotNull Class<V> valueClazz;

    @Nullable LinkedHashMap<K, V> valueCache;

    public ConfigValueMap(@NotNull String configSection, @NotNull Function<String, K> keyCast,
                          @NotNull Class<V> valueClazz) {
        this(ConfigManager.getPluginConfig(), configSection, keyCast, valueClazz);
    }

    public ConfigValueMap(@NotNull FileConfig configuration, @NotNull String configSection,
                          @NotNull Function<String, K> keyCast, @NotNull Class<V> valueClazz) {
        this.source = configuration;
        this.configSection = configSection;
        this.keyCast = keyCast;
        this.valueClazz = valueClazz;
    }


    public @NotNull FileConfiguration getConfiguration() {
        return this.source.getConfig();
    }

    public void clearCache() {
        this.valueCache = null;
    }

    @NotNull
    public Map<K, V> get() {
        if (valueCache != null) return valueCache;
        ConfigurationSection section = getConfiguration().getConfigurationSection(this.configSection);
        if (section == null) return new LinkedHashMap<>();
        Set<String> keys = section.getKeys(false);
        if (keys.isEmpty()) return new LinkedHashMap<>();
        else {
            LinkedHashMap<K, V> result = new LinkedHashMap<>();
            for (String key : keys) {
                K finalKey = keyCast.apply(key);
                Object val = section.get(key);
                V finalValue = this.valueClazz.isInstance(val) ? this.valueClazz.cast(val) : null;
                if (finalKey != null && finalValue != null) {
                    result.put(finalKey, finalValue);
                }
            }
            this.valueCache = result;
            return result;
        }
    }

    public void set(LinkedHashMap<K, V> valuesMap) {
        this.valueCache = valuesMap;
        getConfiguration().createSection(this.configSection, valuesMap);
        this.save();
    }

    public void save() {
        this.source.save();
    }

}
