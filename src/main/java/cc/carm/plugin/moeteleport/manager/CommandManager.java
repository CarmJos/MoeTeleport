package cc.carm.plugin.moeteleport.manager;

import cc.carm.plugin.moeteleport.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public class CommandManager {

    protected final JavaPlugin plugin;

    protected final SimpleCommandMap commandMap;
    protected final Field knownCommandsFiled;

    protected final Map<String, AliasCommand> registeredCommands = new HashMap<>();

    public CommandManager(JavaPlugin plugin) throws Exception {
        this.plugin = plugin;

        SimplePluginManager manager = (SimplePluginManager) Bukkit.getPluginManager();
        Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
        commandMapField.setAccessible(true);
        this.commandMap = (SimpleCommandMap) commandMapField.get(manager);

        this.knownCommandsFiled = SimpleCommandMap.class.getDeclaredField("knownCommands");
        this.knownCommandsFiled.setAccessible(true);

    }

    @SuppressWarnings("unchecked")
    protected Map<String, Command> getKnownCommands() {
        try {
            return (Map<String, Command>) knownCommandsFiled.get(commandMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public String getPrefix() {
        return this.plugin.getName().toLowerCase() + " ";
    }

    protected SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    public void register(String alias, String target) {
        AliasCommand current = this.registeredCommands.get(alias);
        if (current != null) current.unregister(getCommandMap());

        AliasCommand cmd = new AliasCommand(alias, this, getPrefix() + target);
        this.registeredCommands.put(alias, cmd);
        getCommandMap().register(Main.getInstance().getName(), cmd);
    }

    public void unregister(String alias) {
        AliasCommand current = this.registeredCommands.remove(alias);
        if (current != null) {
            getKnownCommands().remove(alias);
            current.unregister(getCommandMap());
        }
    }

    public void unregisterAll() {
        registeredCommands.forEach((k, v) -> {
            getKnownCommands().remove(k);
            v.unregister(getCommandMap());
        });
        registeredCommands.clear();
    }

    public static class AliasCommand extends Command {

        protected final CommandManager commandManager;
        protected final String targetCommand;

        public AliasCommand(String name, CommandManager commandManager, String targetCommand) {
            super(name);
            this.commandManager = commandManager;
            this.targetCommand = targetCommand;
        }

        protected SimpleCommandMap getCommandMap() {
            return this.commandManager.getCommandMap();
        }

        protected String buildCommand(String[] args) {
            return this.targetCommand + " " + String.join(" ", args);
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
            return getCommandMap().dispatch(sender, buildCommand(args));
        }

        @NotNull
        @Override
        public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias,
                                        @NotNull String[] args, @Nullable Location location) throws IllegalArgumentException {
            return Optional.ofNullable(getCommandMap().tabComplete(sender, buildCommand(args))).orElse(Collections.emptyList());
        }

        @NotNull
        @Override
        public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
            return tabComplete(sender, alias, args, null);
        }

    }


}
