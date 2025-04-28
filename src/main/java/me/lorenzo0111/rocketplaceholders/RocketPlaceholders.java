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

import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import me.lorenzo0111.rocketplaceholders.api.IRocketPlaceholdersAPI;
import me.lorenzo0111.rocketplaceholders.api.impl.RocketPlaceholdersAPI;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import me.lorenzo0111.rocketplaceholders.storage.ConfigManager;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import me.lorenzo0111.rocketplaceholders.storage.user.UserStorage;
import me.lorenzo0111.rocketplaceholders.utilities.PluginLoader;
import me.lorenzo0111.rocketplaceholders.web.WebPanelHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

public final class RocketPlaceholders extends JavaPlugin {

    private static RocketPlaceholders instance;
    private static IRocketPlaceholdersAPI api;

    private StorageManager storageManager;
    private PluginLoader loader;
    private File placeholdersDir;

    @Override
    public void onLoad() {
        try {
            long time = System.currentTimeMillis();
            this.getLogger().info("Loading libraries, please wait..");

            File folder = new File(this.getDataFolder(), "libraries");
            if (folder.exists() || folder.mkdirs()) {
                ApplicationBuilder.appending(this.getName())
                        .downloadDirectoryPath(folder.toPath())
                        .mirrorSelector((a, b) -> a) // https://github.com/slimjar/slimjar/issues/61#issuecomment-955549772
                        .internalRepositories(Collections.singleton(new Repository(new URL(SimpleMirrorSelector.ALT_CENTRAL_URL))))
                        .build()
                        .start();

                this.getLogger().info("Loaded all libraries in " + (System.currentTimeMillis() - time) + "ms.");
                return;
            }

        } catch (URISyntaxException | ReflectiveOperationException | NoSuchAlgorithmException | IOException e) {
            this.getLogger().info("An error has occurred while loading dependencies: " + e.getMessage());
        }

        this.getLogger().info("Unable to load dependencies. Disabling..");
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.placeholdersDir = new File(this.getDataFolder(), "placeholders");
        this.storageManager = new StorageManager();

        final ConfigManager placeholders = new ConfigManager(this);
        final PlaceholdersManager placeholdersManager = new PlaceholdersManager(this.storageManager, placeholders, this);
        api = new RocketPlaceholdersAPI(placeholdersManager);

        this.getServer().getServicesManager().register(IRocketPlaceholdersAPI.class, api, this, ServicePriority.Normal);

        try {
            placeholders.registerPlaceholders();
        } catch (IOException | InvalidConditionException e) {
            e.printStackTrace();
        }

        try {
            api.setWebEditor(new WebPanelHandler(this));
        } catch (MalformedURLException e) {
            this.getLogger().warning("Unable to setup WebEditor. Aborting..");
        }

        api.setUserStorage(new UserStorage(new File(this.getDataFolder(), "data.yml")));

        this.loader = new PluginLoader(this, placeholdersManager);
        this.loader.loadChecker();
        this.loader.loadDatabase();
        this.loader.setupHooks();
        this.loader.registerEvents();
        this.loader.placeholderHook();
        this.loader.loadMetrics();
    }

    @Override
    public void onDisable() {
        loader.getFoliaLib().getScheduler().cancelAllTasks();
        api.getUserStorage().save();
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

    public File getPlaceholdersDir() {
        return placeholdersDir;
    }

    public void debug(String text) {
        if (this.getConfig().getBoolean("debug")) {
            this.getLogger().info("Debug: " + text);
        }
    }

    @NotNull
    public static IRocketPlaceholdersAPI getApi() {
        if (api == null) throw new IllegalStateException("API has not been initialized.");

        return api;
    }

    /**
     * @return Plugin instance
     */
    public static RocketPlaceholders getInstance() {
        return instance;
    }
}
