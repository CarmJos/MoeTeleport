package cc.carm.plugin.moeteleport.manager;

import cc.carm.lib.easyplugin.configuration.file.FileConfig;
import cc.carm.lib.easyplugin.configuration.language.MessagesConfig;
import cc.carm.lib.easyplugin.configuration.language.MessagesInitializer;
import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.configuration.PluginMessages;

import java.io.IOException;

public class ConfigManager {

    private static FileConfig config;
    private static MessagesConfig messageConfig;

    public static boolean initConfig() {
        try {
            ConfigManager.config = new FileConfig(Main.getInstance());
            ConfigManager.messageConfig = new MessagesConfig(Main.getInstance());

            FileConfig.pluginConfiguration = () -> config;
            FileConfig.messageConfiguration = () -> messageConfig;

            MessagesInitializer.initialize(messageConfig, PluginMessages.class);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static FileConfig getPluginConfig() {
        return config;
    }

    public static FileConfig getMessageConfig() {
        return messageConfig;
    }

    public static void reload() {
        try {
            getPluginConfig().reload();
            getMessageConfig().reload();
        } catch (Exception ignored) {
        }
    }

    public static void saveConfig() {
        try {
            getPluginConfig().save();
            getMessageConfig().save();
        } catch (Exception ignored) {
        }
    }


}
