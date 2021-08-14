/*
 *  This file is part of RocketPlaceholders, licensed under the MIT License.
 *
 *  Copyright (c) Lorenzo0111
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.lorenzo0111.rocketplaceholders.command;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.command.subcommands.*;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.utilities.Debugger;
import me.lorenzo0111.rocketplaceholders.utilities.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

        subcommands.add(new ReloadCommand(this));
        subcommands.add(new DebugCommand(this));
        subcommands.add(new HelpCommand(this));
        subcommands.add(new ListCommand(this));
        subcommands.add(new InfoCommand(this));
        subcommands.add(new TestCommand(this));
        subcommands.add(new GuiCommand(this));
        subcommands.add(new EditorCommand(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &7Running &e" + plugin.getDescription().getName() + " &ev" + plugin.getDescription().getVersion() + " &7by &eLorenzo0111&7!"));

        if (!sender.hasPermission("rocketplaceholders.command")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &cYou do not have the permission to execute this command."));
            return true;
        }

        if (args.length > 0){
            for (int i = 0; i < getSubcommands().size(); i++){
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                    getSubcommands().get(i).perform(sender, args);
                    return true;
                }
            }

        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &7Use &8/rocketplaceholders help&7 for a command list"));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + "&r &7Command not found, use &8/rocketplaceholders help&7 for a command list"));

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {

        final List<String> strings = new ArrayList<>();

        if (args.length == 0) {
            return subcommands.stream()
                    .map(SubCommand::getName)
                    .collect(Collectors.toList());
        }

        for (SubCommand subCommand : subcommands){
            if (subCommand.getName().toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT))) strings.add(subCommand.getName());
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
