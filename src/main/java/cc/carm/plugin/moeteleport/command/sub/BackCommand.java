package cc.carm.plugin.moeteleport.command.sub;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.moeteleport.MoeTeleport;
import cc.carm.plugin.moeteleport.command.MainCommands;
import cc.carm.plugin.moeteleport.conf.PluginConfig;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.storage.UserData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BackCommand extends SubCommand<MainCommands> {

    public BackCommand(@NotNull MainCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            PluginMessages.NOT_PLAYER.send(sender);
            return null;
        }

        if (!PluginConfig.BACK.ENABLE.getNotNull()) {
            PluginMessages.NOT_ENABLED.send(sender);
            return null;
        }

        Player player = (Player) sender;
        UserData data = MoeTeleport.getUserManager().getData(player);
        if (data.getLastLocation() == null) {
            PluginMessages.BACK.NO_LAST_LOCATION.send(player);
            return null;
        }
        MoeTeleport.getTeleportManager().queueTeleport(player, data.getLastLocation());
        return null;
    }

}
