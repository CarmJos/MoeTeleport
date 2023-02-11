package cc.carm.plugin.moeteleport.command.parent;

import cc.carm.lib.easyplugin.command.CommandHandler;
import cc.carm.plugin.moeteleport.command.MainCommands;
import cc.carm.plugin.moeteleport.command.teleport.TeleportHandleCommand;
import cc.carm.plugin.moeteleport.command.teleport.TeleportRequestCommand;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import cc.carm.plugin.moeteleport.model.TeleportRequest;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TeleportCommands extends CommandHandler {

    protected final @NotNull MainCommands main;

    public TeleportCommands(@NotNull JavaPlugin plugin, @NotNull MainCommands main,
                            @NotNull String cmd, @NotNull String... aliases) {
        super(plugin, cmd, aliases);
        this.main = main;

        registerSubCommand(new TeleportRequestCommand(this, TeleportRequest.RequestType.TPA, "to"));
        registerSubCommand(new TeleportRequestCommand(this, TeleportRequest.RequestType.TPA_HERE, "here"));
        registerSubCommand(new TeleportHandleCommand(this, true, "accept"));
        registerSubCommand(new TeleportHandleCommand(this, false, "deny", "refuse"));
    }

    @Override
    public Void noArgs(CommandSender sender) {
        PluginMessages.USAGE.TELEPORT.send(sender);
        return null;
    }

    @Override
    public Void noPermission(CommandSender sender) {
        return main.noPermission(sender);
    }

}
