package me.lorenzo0111.rocketplaceholders.utilities;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.command.MainCommand;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholderCreator;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.database.DatabaseManager;
import me.lorenzo0111.rocketplaceholders.listener.JoinListener;
import me.lorenzo0111.rocketplaceholders.updater.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class PluginLoader {

    private final RocketPlaceholders plugin;
    private final PlaceholdersManager placeholdersManager;
    private final UpdateChecker updateChecker;

    @Nullable
    private DatabaseManager databaseManager;

    public PluginLoader(RocketPlaceholders plugin, PlaceholdersManager placeholdersManager, UpdateChecker updateChecker) {
        this.plugin = plugin;
        this.placeholdersManager = placeholdersManager;
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

        command.setExecutor(new MainCommand(plugin,placeholdersManager, updateChecker));
        command.setTabCompleter(new MainCommand(plugin,placeholdersManager, updateChecker));
    }

    public void placeholderHook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            plugin.getLogger().info("PlaceholderAPI hooked!");
            plugin.getLogger().info(plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + " by Lorenzo0111 is now enabled!");
            new PlaceholderCreator(plugin, placeholdersManager).register();
            return;
        }

        plugin.getLogger().severe("Could not find PlaceholderAPI! This plugin is required.");
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    public void loadDatabase() {
        ConfigurationSection mysqlSection = plugin.getConfig().getConfigurationSection("mysql");

        if (mysqlSection != null && mysqlSection.getBoolean("enabled")) {
            if (databaseManager == null) {
                this.loadDatabaseManager();

                databaseManager.createTables();

                if (databaseManager.isMain()) {
                    plugin.getLogger().info("Adding placeholders to the database..");

                    databaseManager.removeAll().thenAccept(success -> {
                        if (success) {
                            databaseManager.sync();
                        } else {
                            plugin.getLogger().severe("An error has occurred while adding placeholders to the database, please try again or open an issue on github.");
                        }
                    });
                } else {
                    plugin.getLogger().info("Retrieving placeholders from the database..");

                    databaseManager.getFromDatabase().thenAccept(placeholders -> {
                        plugin.getStorageManager().getInternalPlaceholders().getHashMap().putAll(placeholders);
                        plugin.getLogger().info("Loaded " + placeholders.size() + " placeholders from the database!");
                    });
                }
            }
        }
    }

    public void loadDatabaseManager() {
        databaseManager = new DatabaseManager(plugin);
    }

    public @Nullable DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public void setDatabaseManager(@Nullable DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
}
