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

package me.lorenzo0111.rocketplaceholders.updater;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {

    /*

    Plugin by Lorenzo0111 - https://github.com/Lorenzo0111

     */

    private final JavaPlugin plugin;
    private final int resourceId;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }

    public void sendUpdateCheck(CommandSender player) {
        this.getVersion(version -> {
            if (!this.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l&m---------------------------------------"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lRocket&e&lPlaceholders &f&l» &7There is a new update available."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lRocket&e&lPlaceholders &f&l» &7Download it from: &ehttps://bit.ly/RocketPlaceholders"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l&m---------------------------------------"));
            }
        });
    }

    public void updateCheck() {
        this.getVersion(version -> {
            if (!this.plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                this.plugin.getLogger().info("There is a new update available. Download it from: https://bit.ly/RocketJoin");
            }
        });
    }
}