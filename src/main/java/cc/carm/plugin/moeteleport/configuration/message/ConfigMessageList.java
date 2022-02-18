package cc.carm.plugin.moeteleport.configuration.message;

import cc.carm.plugin.moeteleport.configuration.values.ConfigValueList;
import cc.carm.plugin.moeteleport.manager.ConfigManager;
import cc.carm.plugin.moeteleport.util.MessageUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class ConfigMessageList extends ConfigValueList<String> {

    public ConfigMessageList(String configSection) {
        super(ConfigManager.getMessageConfig(), configSection, String.class);
    }

    public ConfigMessageList(String configSection, String[] defaultValue) {
        super(ConfigManager.getMessageConfig(), configSection, String.class, defaultValue);
    }

    public void send(@Nullable CommandSender sender) {
        MessageUtil.send(sender, get());
    }

    public void sendWithPlaceholders(@Nullable CommandSender sender) {
        MessageUtil.sendWithPlaceholders(sender, get());
    }

    public void sendWithPlaceholders(@Nullable CommandSender sender, String[] params, Object[] values) {
        MessageUtil.sendWithPlaceholders(sender, get(), params, values);
    }
}
