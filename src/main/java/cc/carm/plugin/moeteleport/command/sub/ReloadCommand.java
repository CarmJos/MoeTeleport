package cc.carm.plugin.moeteleport.command.sub;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.moeteleport.Main;
import cc.carm.plugin.moeteleport.command.MainCommands;
import cc.carm.plugin.moeteleport.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand<MainCommands> {

    public ReloadCommand(@NotNull MainCommands parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {

        PluginMessages.RELOAD.START.send(sender);
        long s1 = System.currentTimeMillis();

        try {
            Main.getInstance().getConfigProvider().reload();
            Main.getInstance().getMessageProvider().reload();

            PluginMessages.RELOAD.COMPLETE.send(sender, System.currentTimeMillis() - s1);
        } catch (Exception ex) {
            PluginMessages.RELOAD.ERROR.send(sender);
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("moeteleport.reload");
    }

}
