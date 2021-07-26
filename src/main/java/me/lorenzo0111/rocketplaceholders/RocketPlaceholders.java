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

import me.lorenzo0111.rocketplaceholders.api.IRocketPlaceholdersAPI;
import me.lorenzo0111.rocketplaceholders.api.impl.RocketPlaceholdersAPI;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import me.lorenzo0111.rocketplaceholders.storage.ConfigManager;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import me.lorenzo0111.rocketplaceholders.utilities.PluginLoader;
import me.lorenzo0111.rocketplaceholders.utilities.UpdateChecker;
import me.lorenzo0111.rocketplaceholders.web.WebPanelHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public final class RocketPlaceholders extends JavaPlugin {

    private StorageManager storageManager;
    private PluginLoader loader;
    private static RocketPlaceholders instance;
    private WebPanelHandler web;
    private File placeholdersDir;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.placeholdersDir = new File(this.getDataFolder(), "placeholders");
        this.storageManager = new StorageManager();

        final ConfigManager placeholders = new ConfigManager(this);
        final PlaceholdersManager placeholdersManager = new PlaceholdersManager(this.storageManager, placeholders, this);
        final IRocketPlaceholdersAPI api = new RocketPlaceholdersAPI(placeholdersManager);

        this.getServer().getServicesManager().register(IRocketPlaceholdersAPI.class, api, this, ServicePriority.Normal);

        try {
            placeholders.registerPlaceholders();
        } catch (IOException | InvalidConditionException e) {
            e.printStackTrace();
        }

        try {
            this.web = new WebPanelHandler(this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        final UpdateChecker checker = new UpdateChecker(this, 82678, "https://bit.ly/RocketPlaceholders");
        checker.sendUpdateCheck(Bukkit.getConsoleSender());

        this.loader = new PluginLoader(this, placeholdersManager, checker);
        this.loader.loadDatabase();
        this.loader.setupHooks();
        this.loader.registerEvents();
        this.loader.placeholderHook();
        this.loader.loadMetrics();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        getLogger().info("Plugin disabled! Thanks for using " + this.getDescription().getName() + " v." + this.getDescription().getVersion());
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

    /**
     * @return Plugin instance
     */
    public static RocketPlaceholders getInstance() {
        return instance;
    }

    public WebPanelHandler getWeb() {
        return web;
    }

    public File getPlaceholdersDir() {
        return placeholdersDir;
    }
}
