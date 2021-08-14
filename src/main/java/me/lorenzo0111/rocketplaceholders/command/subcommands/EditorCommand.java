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

import me.lorenzo0111.rocketplaceholders.api.IWebPanelHandler;
import me.lorenzo0111.rocketplaceholders.api.WebEdit;
import me.lorenzo0111.rocketplaceholders.command.RocketPlaceholdersCommand;
import me.lorenzo0111.rocketplaceholders.command.SubCommand;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidResponseException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class EditorCommand extends SubCommand {

    public EditorCommand(RocketPlaceholdersCommand command) {
        super(command);
    }

    @Override
    public String getName() {
        return "editor";
    }

    @Override
    public void perform(@NotNull CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getCommand().getPlugin(), () -> {
            if (!sender.hasPermission("rocketplaceholders.command.editor")) {
                this.sendPermissionsError(sender);
                return;
            }

            IWebPanelHandler web = this.getCommand().getPlugin().getWeb();

            if (args.length != 2) {
                try {
                    String link = web.save();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7Editor URL: " + link));
                } catch (IOException e) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7An error has occurred while loading editor session."));
                    this.getCommand().getPlugin().getLogger().warning(e.getMessage());
                }

                return;
            }

            String code = args[1];
            try {
                WebEdit edit = web.load(code);
                this.getCommand().getPlugin().importEdit(edit);
            } catch (InvalidResponseException e) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7An error has occurred while loading editor session."));
                this.getCommand().getPlugin().getLogger().warning(e.getMessage());
            }
        });
    }
}
