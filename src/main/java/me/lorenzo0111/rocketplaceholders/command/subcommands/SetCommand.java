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
import me.lorenzo0111.rocketplaceholders.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class SetCommand extends SubCommand {

    public SetCommand(RocketPlaceholdersCommand command) {
        super(command);
    }

    @Override
    public String getName() {
        return "set";
    }

    @SuppressWarnings("deprecation")
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!sender.hasPermission("rocketplaceholders.command.set")) {
            this.sendPermissionsError(sender);
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7Try to use &8/rocketplaceholders set (Placeholder) [--user Username] (New Text)!"));
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

        builder.deleteCharAt(builder.length() - 1);


        if (user != null) {
            RocketPlaceholders.getApi().getUserStorage().setText(placeholder,user.getUniqueId(), builder.toString());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7The placeholder &e" + placeholder.getIdentifier() + "&7's text has been set to &e" + builder + "&7 for &e" + user.getName() + "&7!"));
            return;
        }

        if (!placeholder.setText(builder.toString())) {
            DatabaseManager databaseManager = this.getCommand().getPlugin().getLoader().getDatabaseManager();
            if (databaseManager == null || !databaseManager.isMain()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &cAn error has occurred when editing this value. Please try again later!"));

                String reason = "Could not find a reason for the error.";
                if (databaseManager == null) {
                    if (placeholder.getSettings().key() == null) {
                        reason = "The file is not loaded!";
                    }
                } else {
                    reason = "This is not the main server for the database.";
                }

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &cReason: " + reason));
                return;
            }

            databaseManager.removeAll().thenAccept(v -> databaseManager.sync());
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7The placeholder &e" + placeholder.getIdentifier() + "&7's text has been set to &e" + builder + "&7!"));
    }
}
