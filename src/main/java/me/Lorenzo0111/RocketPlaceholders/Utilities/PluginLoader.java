package me.Lorenzo0111.RocketPlaceholders.Utilities;

import me.Lorenzo0111.RocketPlaceholders.Command.MainCommand;
import me.Lorenzo0111.RocketPlaceholders.Creator.PlaceholderCreator;
import me.Lorenzo0111.RocketPlaceholders.Creator.InternalPlaceholders;
import me.Lorenzo0111.RocketPlaceholders.Listener.JoinListener;
import me.Lorenzo0111.RocketPlaceholders.RocketPlaceholders;
import me.Lorenzo0111.RocketPlaceholders.Updater.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;

public class PluginLoader {

    private final RocketPlaceholders plugin;
    private final InternalPlaceholders internalPlaceholders;
    private final UpdateChecker checker;

    public PluginLoader(RocketPlaceholders plugin, InternalPlaceholders internalPlaceholders, UpdateChecker checker) {
        this.plugin = plugin;
        this.internalPlaceholders = internalPlaceholders;
        this.checker = checker;
    }

    public void loadMetrics() {
        new Metrics(plugin, 9381);
    }

    public void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(plugin), plugin);

        plugin.getCommand("rocketplaceholders").setExecutor(new MainCommand(plugin,internalPlaceholders,checker));

        plugin.getCommand("rocketplaceholders").setTabCompleter(new MainCommand(plugin,internalPlaceholders, checker));
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
