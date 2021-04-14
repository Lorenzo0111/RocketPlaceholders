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
import me.lorenzo0111.rocketplaceholders.command.RocketPlaceholdersCommand;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.creator.providers.MVdWPlaceholderAPICreator;
import me.lorenzo0111.rocketplaceholders.creator.providers.PlaceholderAPICreator;
import me.lorenzo0111.rocketplaceholders.database.DatabaseManager;
import me.lorenzo0111.rocketplaceholders.listener.JoinListener;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PluginLoader {

    private final RocketPlaceholders plugin;
    private final PlaceholdersManager placeholdersManager;
    private final UpdateChecker updateChecker;
    private Economy economy = null;
    private DatabaseManager databaseManager;
    private HookType hookType = HookType.NULL;

    public PluginLoader(RocketPlaceholders plugin, PlaceholdersManager placeholdersManager, UpdateChecker updateChecker) {
        this.plugin = plugin;
        this.placeholdersManager = placeholdersManager;
        this.updateChecker = updateChecker;
    }

    public void loadMetrics() {
        final Metrics metrics = new Metrics(plugin, 9381);
        metrics.addCustomChart(new SimplePie("placeholders", () -> String.valueOf(this.placeholdersManager.getStorageManager().getAll().size())));
        metrics.addCustomChart(new SimplePie("hook", () -> this.hookType.getPlugin()));
    }

    public void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(plugin), plugin);

        final PluginCommand command = Objects.requireNonNull(plugin.getCommand("rocketplaceholders"), "Command cannot be null");

        command.setExecutor(new RocketPlaceholdersCommand(plugin,placeholdersManager, updateChecker));
        command.setTabCompleter(new RocketPlaceholdersCommand(plugin,placeholdersManager, updateChecker));
    }

    public void placeholderHook() {
        boolean hook = false;

        if (Bukkit.getPluginManager().getPlugin("MVdWPlaceholderAPI") != null) {
            new MVdWPlaceholderAPICreator(plugin, placeholdersManager);
            this.hookType = HookType.MVDW;
            hook = true;
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.plugin.getLogger().info("PlaceholderAPI hooked!");
            this.plugin.getLogger().info(this.plugin.getDescription().getName() + " v" + this.plugin.getDescription().getVersion() + " by Lorenzo0111 is now enabled!");
            new PlaceholderAPICreator(plugin, placeholdersManager).register();
            this.hookType = HookType.PLACEHOLDERAPI;
            hook = true;
        }

        if (!hook) {
            this.plugin.getLogger().severe("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

    }

    public void setupHooks() {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                this.economy = rsp.getProvider();
                this.plugin.getLogger().info("Hooked with Vault.");
            }
        }
    }

    public void loadDatabase() {
        final ConfigurationSection mysqlSection = plugin.getConfig().getConfigurationSection("mysql");

        if (mysqlSection != null && mysqlSection.getBoolean("enabled") && this.databaseManager == null) {
            this.loadDatabaseManager();

            this.databaseManager.createTables();

            if (databaseManager.isMain()) {
                this.plugin.getLogger().info("Adding placeholders to the database..");

                databaseManager.removeAll().thenAccept(success -> {
                    if (success) {
                        this.databaseManager.sync();
                    } else {
                        this.plugin.getLogger().severe("An error has occurred while adding placeholders to the database, please try again or open an issue on github.");
                    }
                });
            } else {
                this.plugin.getLogger().info("Retrieving placeholders from the database..");

                this.databaseManager.getFromDatabase().thenAccept(placeholders -> {
                    final StorageManager storageManager = Objects.requireNonNull(plugin.getStorageManager(),"StorageManager cannot be null");

                    storageManager.getInternalPlaceholders().getMap().putAll(placeholders);
                    this.plugin.getLogger().info("Loaded " + placeholders.size() + " placeholders from the database!");
                });
            }
        }
    }

    public void loadDatabaseManager() {
        databaseManager = new DatabaseManager(plugin);
    }

    @Nullable
    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public void setDatabaseManager(@Nullable DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public UpdateChecker getUpdater() {
        return updateChecker;
    }

    @Nullable
    public Economy getEconomy() {
        return economy;
    }

    public HookType getHookType() {
        return hookType;
    }
}
