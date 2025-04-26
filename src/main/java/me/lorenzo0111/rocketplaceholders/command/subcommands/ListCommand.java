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

import me.lorenzo0111.rocketplaceholders.command.RocketPlaceholdersCommand;
import me.lorenzo0111.rocketplaceholders.command.SubCommand;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ListCommand extends SubCommand {

    public ListCommand(RocketPlaceholdersCommand command) {
        super(command);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!sender.hasPermission("rocketplaceholders.command.list")) {
            this.sendPermissionsError(sender);
            return;
        }


        this.getCommand().getPlugin().getLoader().getFoliaLib().getScheduler().runAsync((task) -> {
            final Map<String, Placeholder> internalPlaceholders = ListCommand.this.getCommand().getPlaceholdersManager().getStorageManager().getInternalPlaceholders().getMap();
            final Map<String, Placeholder> externalPlaceholders = ListCommand.this.getCommand().getPlaceholdersManager().getStorageManager().getExternalPlaceholders().getMap();

            StringBuilder internalBuilder = new StringBuilder("(");
            internalPlaceholders.forEach((identifier, placeholder) -> internalBuilder.append(identifier).append(","));

            if (internalBuilder.length() > 1) {
                internalBuilder.setLength(internalBuilder.length() - 1);
            }

            internalBuilder.append(")");

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ListCommand.this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7Internal placeholders: &e" + internalBuilder.toString()));

            StringBuilder externalBuilder = new StringBuilder("(");
            externalPlaceholders.forEach((identifier, placeholder) -> externalBuilder.append(identifier).append(","));


            if (externalBuilder.length() > 1) {
                externalBuilder.setLength(externalBuilder.length() - 1);
            }

            externalBuilder.append(")");

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ListCommand.this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7External placeholders: &e" + externalBuilder.toString()));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ListCommand.this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7Use &8/rocketplaceholders info (Placeholder) &7for more information."));
        });
    }
}
