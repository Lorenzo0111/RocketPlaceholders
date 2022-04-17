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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand extends SubCommand {

    public HelpCommand(RocketPlaceholdersCommand command) {
        super(command);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &8/rocketplaceholders help » &7Show this message"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &8/rocketplaceholders reload » &7Reload the plugin"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &8/rocketplaceholders list » &7List all placeholders"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &8/rocketplaceholders info (Placeholder) » &7Get information about a placeholder"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &8/rocketplaceholders gui » &7Open the gui"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &8/rocketplaceholders debug » &7Print debug message"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &8/rocketplaceholders test (Placeholder) » &7Try a placeholder"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &8/rocketplaceholders set (Placeholder) [--user Username] (New Text) » &7Edit the text of a placeholder"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &8/rocketplaceholders setjs (Placeholder) (Javascript Expression) » &7Edit the text of a placeholder"));
    }
}
