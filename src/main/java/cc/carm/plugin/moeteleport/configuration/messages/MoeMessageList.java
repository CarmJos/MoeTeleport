package cc.carm.plugin.moeteleport.configuration.messages;

import cc.carm.lib.easyplugin.configuration.language.EasyMessageList;
import cc.carm.lib.easyplugin.configuration.language.builder.EasyMessageListBuilder;
import de.themoep.minedown.MineDown;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoeMessageList extends EasyMessageList {

    public MoeMessageList(@Nullable String[] defaultValue, @Nullable String[] messageParams) {
        super(defaultValue, messageParams);
    }

    public static EasyMessageListBuilder builder() {
        return new EasyMessageListBuilder() {
            @Override
            public MoeMessageList build() {
                return new MoeMessageList(this.contents, buildParams());
            }
        };
    }

    @Override
    public void send(@Nullable CommandSender sender, @Nullable String[] params, @Nullable Object[] values) {
        if (sender == null) return;
        List<String> messages = get(sender, params, values);
        if (messages.isEmpty()) return;
        if (messages.size() == 1 && messages.get(0).length() == 0) return; //空消息不再发送
        messages.forEach(message -> sender.spigot().sendMessage(MineDown.parse(message)));

    }


}
