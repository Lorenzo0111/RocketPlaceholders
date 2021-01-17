package me.lorenzo0111.rocketplaceholders.utilities;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.command.MainCommand;
import me.lorenzo0111.rocketplaceholders.creator.InternalPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholderCreator;
import me.lorenzo0111.rocketplaceholders.listener.JoinListener;
import me.lorenzo0111.rocketplaceholders.updater.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;

public class PluginLoader {

    private final RocketPlaceholders plugin;
    private final InternalPlaceholders internalPlaceholders;
    private final UpdateChecker updateChecker;

    public PluginLoader(RocketPlaceholders plugin, InternalPlaceholders internalPlaceholders, UpdateChecker updateChecker) {
        this.plugin = plugin;
        this.internalPlaceholders = internalPlaceholders;
        this.updateChecker = updateChecker;
    }

    public void loadMetrics() {
        new Metrics(plugin, 9381);
    }

    public void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(plugin), plugin);

        PluginCommand command = plugin.getCommand("rocketplaceholders");

        if (command == null) {
            plugin.getLogger().severe("An error has occurred, please open an issue on GitHub");
            return;
        }

        command.setExecutor(new MainCommand(plugin,internalPlaceholders, updateChecker));
        command.setTabCompleter(new MainCommand(plugin,internalPlaceholders, updateChecker));
    }

    public void placeholderHook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            plugin.getLogger().info("PlaceholderAPI hooked!");
            plugin.getLogger().info(plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " by Lorenzo0111 is now enabled!");
            new PlaceholderCreator(plugin, internalPlaceholders).register();
            return;
        }

        plugin.getLogger().severe("Could not find PlaceholderAPI! This plugin is required.");
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

}
