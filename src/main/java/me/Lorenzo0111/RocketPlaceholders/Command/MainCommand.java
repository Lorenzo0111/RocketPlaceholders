package me.Lorenzo0111.RocketPlaceholders.Command;

import me.Lorenzo0111.RocketPlaceholders.Creator.InternalPlaceholders;
import me.Lorenzo0111.RocketPlaceholders.RocketPlaceholders;
import me.Lorenzo0111.RocketPlaceholders.Updater.UpdateChecker;
import me.Lorenzo0111.RocketPlaceholders.Utilities.Debugger;
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
    private final InternalPlaceholders internalPlaceholders;
    private final UpdateChecker checker;
    private final Debugger debugger;

    public MainCommand(RocketPlaceholders plugin, InternalPlaceholders internalPlaceholders, UpdateChecker checker) {
        this.plugin = plugin;
        this.internalPlaceholders = internalPlaceholders;
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
                    checker.senderUpdateCheck(sender);
                    internalPlaceholders.reloadPlaceholders();
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