package me.lorenzo0111.rocketplaceholders.command;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.command.subcommands.DebugCommand;
import me.lorenzo0111.rocketplaceholders.command.subcommands.HelpCommand;
import me.lorenzo0111.rocketplaceholders.command.subcommands.ReloadCommand;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.updater.UpdateChecker;
import me.lorenzo0111.rocketplaceholders.utilities.Debugger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RocketPlaceholdersCommand implements CommandExecutor, TabCompleter {

    private final RocketPlaceholders plugin;
    private final PlaceholdersManager placeholdersManager;
    private final UpdateChecker checker;
    private final Debugger debugger;
    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public RocketPlaceholdersCommand(RocketPlaceholders plugin, PlaceholdersManager placeholdersManager, UpdateChecker checker) {
        this.plugin = plugin;
        this.placeholdersManager = placeholdersManager;
        this.checker = checker;
        this.debugger = new Debugger(plugin);

        subcommands.add(new ReloadCommand().create(this));
        subcommands.add(new DebugCommand().create(this));
        subcommands.add(new HelpCommand().create(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &7Running &e" + plugin.getDescription().getName() + " &ev" + plugin.getDescription().getVersion() + " &7by &eLorenzo0111&7!"));

        if (args.length > 0){
            for (int i = 0; i < getSubcommands().size(); i++){
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                    getSubcommands().get(i).perform(sender, args);
                    return true;
                }
            }

        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &8/rocketplaceholders help » &7Show this message!"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &8/rocketplaceholders reload » &7Reload the plugin!"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &8/rocketplaceholders debug » &7Print debug message!"));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &7Command not found, use &8/rocketplaceholders help&7 for a command list"));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        final List<String> strings = new ArrayList<>();

        for (int i = 0; i < getSubcommands().size(); i++){
            if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                strings.add(getSubcommands().get(i).getName());
            }
        }

        return strings;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

    public RocketPlaceholders getPlugin() {
        return plugin;
    }

    public Debugger getDebugger() {
        return debugger;
    }

    public PlaceholdersManager getPlaceholdersManager() {
        return placeholdersManager;
    }

    public UpdateChecker getChecker() {
        return checker;
    }
}
