package me.lorenzo0111.rocketplaceholders.command;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.creator.placeholders.InternalPlaceholders;
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

public class MainCommand implements CommandExecutor, TabCompleter {

    /*

    Plugin by Lorenzo0111 - https://github.com/Lorenzo0111

     */

    private final RocketPlaceholders plugin;
    private final PlaceholdersManager placeholdersManager;
    private final UpdateChecker checker;
    private final Debugger debugger;

    public MainCommand(RocketPlaceholders plugin, PlaceholdersManager placeholdersManager, UpdateChecker checker) {
        this.plugin = plugin;
        this.placeholdersManager = placeholdersManager;
        this.checker = checker;
        this.debugger = new Debugger(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rocketplaceholders")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &7Running &e" + plugin.getDescription().getName() + " &ev" + plugin.getDescription().getVersion() + " &7by &eLorenzo0111&7!"));
            if (!sender.hasPermission("rocketplaceholders.command")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + " " + plugin.getConfig().getString("no_permission")));
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &8/rocketplaceholders help » &7Show this message!"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &8/rocketplaceholders reload » &7Reload the plugin!"));
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reloadConfig();
                    checker.sendUpdateCheck(sender);
                    placeholdersManager.reload();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &7Plugin reloaded!"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &8/rocketplaceholders help » &7Show this message!"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &8/rocketplaceholders reload » &7Reload the plugin!"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("debug")) {
                    debugger.debug();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &7Debug informations printed in the console."));
                    return true;
                }
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &7Command not found, use &8/rocketplaceholders help&7 for a command list"));
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        List<String> strings = new ArrayList<>();

        strings.add("reload");
        strings.add("help");
        strings.add("debug");

        return strings;
    }
}