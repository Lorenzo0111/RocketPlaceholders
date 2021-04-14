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

package me.lorenzo0111.rocketplaceholders;

import me.lorenzo0111.rocketplaceholders.api.RocketPlaceholdersAPI;
import me.lorenzo0111.rocketplaceholders.api.RocketPlaceholdersAPIManager;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.storage.ConfigManager;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import me.lorenzo0111.rocketplaceholders.utilities.PluginLoader;
import me.lorenzo0111.rocketplaceholders.utilities.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class RocketPlaceholders extends JavaPlugin {

    private StorageManager storageManager;
    private PluginLoader loader;

    /*

    Plugin by Lorenzo0111 - https://github.com/Lorenzo0111

     */

    @Override
    public void onEnable() {

        saveDefaultConfig();

        this.storageManager = new StorageManager(this);

        final ConfigManager placeholders = new ConfigManager(this);
        final PlaceholdersManager placeholdersManager = new PlaceholdersManager(this.storageManager, placeholders, this);
        final RocketPlaceholdersAPI api = new RocketPlaceholdersAPIManager(placeholdersManager);

        this.getServer().getServicesManager().register(RocketPlaceholdersAPI.class, api, this, ServicePriority.Normal);

        placeholders.registerPlaceholders();

        final UpdateChecker checker = new UpdateChecker(this, 82678);
        checker.updateCheck();

        this.loader = new PluginLoader(this, placeholdersManager, checker);
        this.loader.loadDatabase();
        this.loader.setupHooks();
        this.loader.registerEvents();
        this.loader.placeholderHook();
        this.loader.loadMetrics();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled! Thanks for using " + this.getDescription().getName() + " v." + this.getDescription().getVersion());
        Bukkit.getScheduler().cancelTasks(this);
    }

    /**
     * @return Storage manager with internal and external placeholders
     */
    public StorageManager getStorageManager() {
        return this.storageManager;
    }

    public PluginLoader getLoader() {
        return this.loader;
    }

    public void debug(String text) {
        if (this.getConfig().getBoolean("debug")) {
            this.getLogger().info("Debug: " + text);
        }
    }
}
