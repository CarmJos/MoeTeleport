package cc.carm.plugin.moeteleport.configuration.messages;

import cc.carm.lib.easyplugin.configuration.language.EasyMessage;
import cc.carm.lib.easyplugin.configuration.language.builder.EasyMessageBuilder;
import de.themoep.minedown.MineDown;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class MoeMessage extends EasyMessage {

    public MoeMessage(@Nullable String defaultValue, @Nullable String[] messageParams) {
        super(defaultValue, messageParams);
    }

    public static EasyMessageBuilder builder() {
        return new EasyMessageBuilder() {
            @Override
            public MoeMessage build() {
                return new MoeMessage(this.content, buildParams());
            }
        };
    }

    @Override
    public void send(@Nullable CommandSender sender, @Nullable String[] params, @Nullable Object[] values) {
        if (sender == null) return;
        String message = get(sender, params, values);
        if (message.length() < 1) return;
        sender.spigot().sendMessage(MineDown.parse(message));
    }


}
