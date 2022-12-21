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

package me.lorenzo0111.rocketplaceholders.command.subcommands;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.command.RocketPlaceholdersCommand;
import me.lorenzo0111.rocketplaceholders.command.SubCommand;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.exceptions.SaveException;
import me.lorenzo0111.rocketplaceholders.utilities.JavaScriptParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import javax.script.ScriptException;

public class SetJSCommand extends SubCommand {
    private final JavaScriptParser<String> engine;

    public SetJSCommand(RocketPlaceholdersCommand command) {
        super(command);
        this.engine = new JavaScriptParser<>();
    }

    @Override
    public String getName() {
        return "setjs";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void perform(CommandSender sender, String[] args) {
        if (!sender.hasPermission("rocketplaceholders.command.setjs")) {
            this.sendPermissionsError(sender);
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7Try to use &8/rocketplaceholders setjs (Placeholder) (Javascript Expression)!"));
            return;
        }

        Placeholder placeholder = this.getCommand().getPlaceholdersManager().getStorageManager().get(args[1]);

        if (placeholder == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7This placeholder does not exists!"));
            return;
        }

        OfflinePlayer user = null;
        if (args.length >= 5 && args[2].equalsIgnoreCase("--user")) {
            user = Bukkit.getOfflinePlayer(args[3]);
        }

        StringBuilder builder = new StringBuilder();
        for (int i = user != null ? 4 : 2; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }

        int runner = user != null ? 2 : 1;

        builder.deleteCharAt(builder.length() - 1);
        for (int i = 0; i < runner; i++) {
            try {
                engine.bind("Player", sender);
                engine.bind("Server", Bukkit.getServer());
                engine.bind("Placeholder", placeholder);
                String text = engine.parse(builder.toString());
                if (text != null) {

                    if (user != null) {
                        RocketPlaceholders.getApi().getUserStorage().setText(placeholder,user.getUniqueId(),text);
                    } else {
                        try {
                            placeholder.saveText(text);
                        } catch (SaveException e) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &cAn error has occurred when editing this value. Please try again later!"));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &cReason: " + e.getReason()));
                            return;
                        }
                    }

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7The placeholder &e" + placeholder.getIdentifier() + "&7's text has been set to &e" + text + "&7!"));
                }
            } catch (ScriptException e) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7Error while parsing the javascript expression!"));
                this.getCommand().getPlugin().debug(e.getMessage());
            }
        }
    }
}
