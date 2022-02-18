package cc.carm.plugin.moeteleport.configuration.message;

import cc.carm.plugin.moeteleport.configuration.values.ConfigValue;
import cc.carm.plugin.moeteleport.manager.ConfigManager;
import cc.carm.plugin.moeteleport.util.MessageUtil;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ConfigMessage extends ConfigValue<String> {

    public ConfigMessage(String configSection) {
        this(configSection, null);
    }

    public ConfigMessage(String configSection, String defaultValue) {
        super(ConfigManager.getMessageConfig(), configSection, String.class, defaultValue);
    }

    public void send(CommandSender sender) {
        MessageUtil.send(sender, get());
    }

    public void sendWithPlaceholders(CommandSender sender) {
        MessageUtil.sendWithPlaceholders(sender, get());
    }

    public void sendWithPlaceholders(CommandSender sender, String[] params, Object[] values) {
        MessageUtil.sendWithPlaceholders(sender, Collections.singletonList(get()), params, values);
    }

}
