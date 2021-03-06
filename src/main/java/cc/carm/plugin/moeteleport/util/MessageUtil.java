package cc.carm.plugin.moeteleport.util;

import de.themoep.minedown.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MessageUtil {

    public static boolean hasPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static void send(@Nullable CommandSender sender, List<String> messages) {
        if (messages == null || messages.isEmpty() || sender == null) return;
        for (String s : messages) {
            sender.spigot().sendMessage(MineDown.parse(s));
        }
    }

    public static void send(@Nullable CommandSender sender, String... messages) {
        send(sender, Arrays.asList(messages));
    }

    public static void sendWithPlaceholders(CommandSender sender, String... messages) {
        sendWithPlaceholders(sender, Arrays.asList(messages));
    }

    public static void sendWithPlaceholders(@Nullable CommandSender sender, List<String> messages) {
        if (messages == null || messages.isEmpty() || sender == null) return;
        if (hasPlaceholderAPI() && sender instanceof Player) {
            send(sender, PlaceholderAPI.setPlaceholders((Player) sender, messages));
        } else {
            send(sender, messages);
        }
    }

    public static void sendWithPlaceholders(@Nullable CommandSender sender, List<String> messages, String param, Object value) {
        sendWithPlaceholders(sender, messages, new String[]{param}, new Object[]{value});
    }

    public static void sendWithPlaceholders(@Nullable CommandSender sender, List<String> messages, String[] params, Object[] values) {
        sendWithPlaceholders(sender, setCustomParams(messages, params, values));
    }

    public static List<String> setCustomParams(List<String> messages, String param, Object value) {
        return setCustomParams(messages, new String[]{param}, new Object[]{value});
    }

    public static List<String> setCustomParams(List<String> messages, String[] params, Object[] values) {
        if (params.length != values.length) return messages;
        HashMap<String, Object> paramsMap = new HashMap<>();
        for (int i = 0; i < params.length; i++) {
            paramsMap.put(params[i], values[i]);
        }
        return setCustomParams(messages, paramsMap);
    }


    public static List<String> setCustomParams(List<String> messages, HashMap<String, Object> params) {
        List<String> list = new ArrayList<>();
        for (String message : messages) {
            String afterMessage = message;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                afterMessage = afterMessage.replace(entry.getKey(), entry.getValue().toString());
            }
            list.add(afterMessage);
        }
        return list;
    }


}
