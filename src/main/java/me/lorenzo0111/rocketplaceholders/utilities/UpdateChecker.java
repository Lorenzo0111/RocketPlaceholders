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

package me.lorenzo0111.rocketplaceholders.utilities;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {

    private boolean updateAvailable;
    private final RocketPlaceholders plugin;
    private final int resourceId;
    private final String url;

    public UpdateChecker(RocketPlaceholders plugin, int resourceId, String url) {
        this.plugin = plugin;
        this.resourceId = resourceId;
        this.url = url;

        if (this.plugin.getDescription().getVersion().endsWith("-SNAPSHOT") || this.plugin.getDescription().getVersion().endsWith("-BETA")) {
            this.plugin.getLogger().info("Running a SNAPSHOT or BETA version, the updater may be bugged here.");
        }

        this.fetch();
    }

    private void fetch() {
        plugin.getLoader().getFoliaLib().getScheduler().runAsync((task) -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    String version = scanner.next();

                    this.updateAvailable = !this.plugin.getDescription().getVersion().equalsIgnoreCase(version);
                }
            } catch (IOException exception) {
                this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }

    public void sendUpdateCheck(CommandSender player) {
        if (updateAvailable) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&8[&eRocketUpdater&8] &7An update of %s is available. Download it from %s",plugin.getDescription().getName(),url)));
        }
    }
}