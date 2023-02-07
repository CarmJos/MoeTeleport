package cc.carm.plugin.moeteleport.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageListBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageValueBuilder;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import de.themoep.minedown.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class PluginMessages extends ConfigurationRoot {

    public static @NotNull CraftMessageListBuilder<BaseComponent[]> list() {
        return ConfiguredMessageList.create(getParser())
                .whenSend((sender, message) -> message.forEach(m -> sender.spigot().sendMessage(m)));
    }

    public static @NotNull CraftMessageValueBuilder<BaseComponent[]> value() {
        return ConfiguredMessage.create(getParser())
                .whenSend((sender, message) -> sender.spigot().sendMessage(message));
    }

    public static @NotNull BiFunction<CommandSender, String, BaseComponent[]> getParser() {
        return (sender, message) -> {
            if (sender instanceof Player) message = PlaceholderAPI.setPlaceholders((Player) sender, message);
            return MineDown.parse(ColorParser.parse(message));
        };
    }


    public static final ConfiguredMessageList<BaseComponent[]> NOT_ONLINE = list().defaults(
            "&f目标玩家并不在线，无法发送请求。"
    ).build();

    public static class Back {

        public static final ConfiguredMessageList<BaseComponent[]> NO_LAST_LOCATION = list().defaults(
                "&f您当前没有进行任何传送，无法返回上个地点。"
        ).build();

        public static final ConfiguredMessageList<BaseComponent[]> DEATH_MESSAGE = list().defaults(
                "&f您可以输入 &5/back &f或 [&d&l点击这里](show_text=点击返回到死亡地点 run_command=/moeteleport:back) &f返回您的死亡地点。"
        ).build();

    }

    public static class Teleport {
        public static final ConfiguredMessageList<BaseComponent[]> TELEPORTING = list().defaults(
                "&f正在将您传送到 &d%(location) &f..."
        ).params("location").build();

        public static final ConfiguredMessageList<BaseComponent[]> NOT_SAFE = list().defaults(
                "&f目标地点 &d%(location) &f并不安全，因此传送被取消。",
                "&7如需传送，请告知请求者到达安全位置后重新发送传送请求。"
        ).params("location").build();

        public static final ConfiguredMessageList<BaseComponent[]> NOT_AVAILABLE = list().defaults(
                "&f目标地点暂时无法前往，传送被取消。"
        ).build();

    }

    public static class Requests {

        public static final ConfiguredMessageList<BaseComponent[]> SELF = list().defaults("&f您不能向自己发送请求。").build();

        public static final ConfiguredMessageList<BaseComponent[]> OFFLINE = list().defaults(
                "&d%(player) &f离线，相关请求已自动取消。"
        ).params("player").build();

        public static final ConfiguredMessageList<BaseComponent[]> MULTI = list().defaults(
                "&f您当前有&d%(num)条请求&f待处理，请输入 &5/%(command) <玩家名> &f决定回应谁的请求。",
                "&f您也可以再次输入 &5/%(command) &f快速回应最近的一条请求。"
        ).params("num", "command").build();


        public static final ConfiguredMessageList<BaseComponent[]> EMPTY_REQUESTS = list()
                .defaults("&f您当前没有任何待处理的传送请求。")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> NO_REQUEST_FROM = list().defaults(
                "&f您当前没有收到来自 &d%(player) &f的传送请求。"
        ).params("player").build();

        public static final ConfiguredMessageList<BaseComponent[]> SENT = list().defaults(
                "&f成功向玩家 &d%(player) &f发送传送请求，对方有 &5%(expire)秒 &f的时间回应该请求。"
        ).params("player", "expire").build();

        public static final ConfiguredMessageList<BaseComponent[]> DUPLICATE = list().defaults(
                "&f您已经向 &d%(player) &f发送过传送请求，对方仍有 &5%(expire)秒 &f的时间回应该请求。"
        ).params("player", "expire").build();

        public static final ConfiguredMessageList<BaseComponent[]> RECEIVED_TP_HERE = list().defaults(
                "&d%(player) &f请求传送到您身边，您有 &5%(expire)秒 &f的时间回应。",
                "  [&a&l[点击同意]](show_text=点击同意请求 run_command=/moeteleport:tpaccept %(player)) &f或输入 &5/tpAccept &f同意该请求。",
                "  [&c&l[点击拒绝]](show_text=点击拒绝请求 run_command=/moeteleport:tpdeny %(player)) &f或输入 &5/tpDeny &f拒绝该请求。"
        ).params("player", "expire").build();

        public static final ConfiguredMessageList<BaseComponent[]> RECEIVED_TP_TO = list().defaults(
                "&d%(player) &f请求传送您到Ta身边，您有 &5%(expire)秒 &f的时间回应。",
                "  [&a&l[点击同意]](show_text=点击同意请求 run_command=/moeteleport:tpaccept %(player)) &f或输入 &5/tpAccept &f同意该请求。",
                "  [&c&l[点击拒绝]](show_text=点击拒绝请求 run_command=/moeteleport:tpdeny %(player)) &f或输入 &5/tpDeny &f拒绝该请求。"
        ).params("player", "expire").build();


        public static final ConfiguredMessageList<BaseComponent[]> ACCEPTED = list().defaults(
                "&f您同意了 &d%(player) &f的传送请求。"
        ).params("player").build();

        public static final ConfiguredMessageList<BaseComponent[]> DENIED = list().defaults(
                "&f您&d拒绝&f了 &d%(player) &f的传送请求。"
        ).params("player").build();

        public static final ConfiguredMessageList<BaseComponent[]> WAS_ACCEPTED = list().defaults(
                "&d%(player) &f同意了您的传送请求。"
        ).params("player").build();

        public static final ConfiguredMessageList<BaseComponent[]> WAS_DENIED = list().defaults(
                "&d%(player) &f拒绝了您的传送请求。"
        ).params("player").build();

        public static final ConfiguredMessageList<BaseComponent[]> SENT_TIMEOUT = list().defaults(
                "&f发往 &d%(player) &f的传送请求已超时。"
        ).params("player").build();

        public static final ConfiguredMessageList<BaseComponent[]> RECEIVED_TIMEOUT = list().defaults(
                "&f来自 &d%(player) &f的传送请求已超时。"
        ).params("player").build();

    }


    public static class Home {

        public static final ConfiguredMessageList<BaseComponent[]> NAME_TOO_LONG = list()
                .defaults("&f您所输入的家的名字太长，家的名称不应当超过 &d32 &f个字符。")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> OVER_LIMIT = list().defaults(
                "&f您最多只能设置 &d%(max) &f个家传送点！",
                "&7可以输入 &5/delHome <家名称> &7删除之前的家传送点，",
                "&7或输入 &5/setHome <家名称> &7覆盖之前的家传送点。"
        ).params("max").build();

        public static final ConfiguredMessageList<BaseComponent[]> NOT_FOUND = list()
                .defaults("&f您还没有设置这个家，请先输入 &5/setHome <家名称> &f设置一个吧！")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> SET = list().defaults(
                "&f成功设定名为 &d%(name) &f的家传送点。"
        ).params("name").build();

        public static final ConfiguredMessageList<BaseComponent[]> OVERRIDE = list().defaults(
                "&f成功覆盖名为 &d%(name) &f的家传送点。",
                "&8原先位置为 &5%(location) &8。"
        ).params("name", "location").build();

        public static final ConfiguredMessageList<BaseComponent[]> REMOVED = list().defaults(
                "&f成功移除名为 &d%(name) &f的家传送点。",
                "&8原先位置为 &5%(location) &8。"
        ).params("name", "location").build();

        public static class List {

            public static final ConfiguredMessageList<BaseComponent[]> HEADER = list().defaults(
                    "&f您当前设定的所有家："
            ).build();

            public static final ConfiguredMessageList<BaseComponent[]> OBJECT = list().defaults(
                    "&8# &f%(id) &d%(location) [&7✈](show_text=点击返回家 %(id) run_command=/moeteleport:home %(id))"
            ).params("id", "location").build();

        }

    }

    public static class Warp {

        public static final ConfiguredMessageList<BaseComponent[]> EMPTY = list().defaults(
                "&f当前服务器暂无任何地标点，快设置一个吧！"
        ).build();

        public static final ConfiguredMessageList<BaseComponent[]> NOT_OWNER = list().defaults(
                "&f您不是地标 &d%(name) &f的创建者，无法进行此操作。"
        ).params("name").build();

        public static final ConfiguredMessageList<BaseComponent[]> NOT_FOUND = list()
                .defaults("&f目前暂不存在该地标。")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> NAME_TOO_LONG = list()
                .defaults("&f您所输入的家的名字太长，地标的名称不应当超过 &d16 &f个字符。")
                .build();

        public static final ConfiguredMessageList<BaseComponent[]> OVER_LIMIT = list().defaults(
                "&f您最多只能设置 &d%(max) &f个地标传送点！",
                "&7可以输入 &5/delWarp <地标名称> &7删除之前的地标传送点，",
                "&7或输入 &5/setWarp <地标名称> &7覆盖之前的地标传送点。"
        ).params("max").build();

        public static final ConfiguredMessageList<BaseComponent[]> INFO_LOCATION = list().defaults(
                "&f地标点 &d%(name) &f所在位置为 &5%(location) &f。[&7✈](show_text=点击前往&d %(name) run_command=/moeteleport:warp %(name))"
        ).params("name", "location").build();

        public static final ConfiguredMessageList<BaseComponent[]> INFO_FULL = list().defaults(
                "&f地标点 &d%(name) &f由 &5%(owner) &f创建，所在位置为 &d%(location) &f。[&7✈](show_text=点击前往&d %(name) run_command=/moeteleport:warp %(name))"
        ).params("name", "owner", "location").build();

        public static final ConfiguredMessageList<BaseComponent[]> SET = list().defaults(
                "&f成功设定名为 &d%(name) &f的地标传送点。"
        ).params("name").build();

        public static final ConfiguredMessageList<BaseComponent[]> OVERRIDE = list().defaults(
                "&f成功覆盖名为 &d%(name) &f的地标传送点。",
                "&8原先位置为 &5%(location) &8。"
        ).params("name", "location").build();

        public static final ConfiguredMessageList<BaseComponent[]> REMOVED = list().defaults(
                "&f成功移除名为 &d%(name) &f的地标传送点。",
                "&8原先位置为 &5%(location) &8。"
        ).params("name", "location").build();


        public static class List {

            public static final ConfiguredMessageList<BaseComponent[]> HEADER = list().defaults(
                    "&f当前地标列表 &7(第&f%(current)&8/%(max)&7页)："
            ).params("current", "max").build();

            public static final ConfiguredMessageList<BaseComponent[]> OBJECT = list().defaults(
                    "&8# &f%(id) &7[由%(owner)创建]",
                    "&8- &d%(location) [&7✈](show_text=点击前往&d %(id) run_command=/moeteleport:warp %(id))"
            ).params("id", "owner", "location").build();

            public static final ConfiguredMessageList<BaseComponent[]> OBJECT_NO_OWNER = list().defaults(
                    "&8# &f%(id)",
                    "&8- &d%(location) [&7✈](show_text=点击前往&d %(id) run_command=/moeteleport:warp %(id))"
            ).params("id", "location").build();

        }


    }


}
