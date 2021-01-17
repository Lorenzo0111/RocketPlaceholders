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
                plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }

    public void sendUpdateCheck(CommandSender player) {
        new UpdateChecker(plugin, resourceId).getVersion(version -> {
            if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l&m---------------------------------------"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lRocket&e&lPlaceholders &f&l» &7There is a new update available."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lRocket&e&lPlaceholders &f&l» &7Download it from: &ehttps://bit.ly/RocketPlaceholders"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l&m---------------------------------------"));
            }
        });
    }

    public void updateCheck() {
        this.getVersion(version -> {
            if (plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                plugin.getLogger().info("There is not a new update available.");
            } else {
                plugin.getLogger().info("There is a new update available. Download it from: https://bit.ly/RocketJoin");
            }
        });
    }
}