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
import me.lorenzo0111.rocketplaceholders.api.IWebPanelHandler;
import me.lorenzo0111.rocketplaceholders.api.WebEdit;
import me.lorenzo0111.rocketplaceholders.api.impl.RocketPlaceholdersAPI;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import me.lorenzo0111.rocketplaceholders.storage.ConfigManager;
import me.lorenzo0111.rocketplaceholders.storage.Storage;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import me.lorenzo0111.rocketplaceholders.utilities.PluginLoader;
import me.lorenzo0111.rocketplaceholders.utilities.UpdateChecker;
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
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class RocketPlaceholders extends JavaPlugin {

    private StorageManager storageManager;
    private PluginLoader loader;
    private static RocketPlaceholders instance;
    private IWebPanelHandler web;
    private File placeholdersDir;
    private static IRocketPlaceholdersAPI api;

    @Override
    public void onLoad() {
        try {
            long time = System.currentTimeMillis();
            this.getLogger().info("Loading libraries, please wait..");

            File folder = new File(this.getDataFolder(), "libraries");
            if (folder.exists() || folder.mkdirs()) {
                ApplicationBuilder.appending(this.getName())
                        .downloadDirectoryPath(folder.toPath())
                        .logger((s, objects) -> this.getLogger().info(MessageFormat.format(s,objects)))
                        .mirrorSelector((a, b) -> a) // https://github.com/slimjar/slimjar/issues/61#issuecomment-955549772
                        .internalRepositories(Collections.singleton(new Repository(new URL(SimpleMirrorSelector.ALT_CENTRAL_URL))))
                        .build()
                        .start();

                this.getLogger().info("Loaded all libraries in " + time + "ms.");
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
            this.web = new WebPanelHandler(this);
        } catch (MalformedURLException e) {
            this.getLogger().warning("Unable to setup WebEditor. Aborting..");
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

    public void importEdit(@NotNull WebEdit edit) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            Storage storage = api.getPlaceholdersManager().getStorageManager().getInternalPlaceholders();

            List<String> remove = edit.getRemove();
            storage.getMap()
                    .entrySet()
                    .removeIf((entry) -> remove.contains(entry.getKey()) && entry.getValue().getFile().delete());

            Map<String, String> rename = edit.getRename();
            for (Map.Entry<String,String> entry : rename.entrySet()) {
                for (Placeholder placeholder : storage.getMap().values()) {
                    if (!placeholder.getIdentifier().equals(entry.getKey())) continue;

                    placeholder.edit("placeholder", entry.getValue());
                    api.getPlaceholdersManager()
                            .getConfigManager()
                            .reload(entry.getKey(), placeholder);
                    break;
                }
            }

            List<Placeholder> edits = edit.getEdited();
            for (Placeholder placeholder : edits) {
                if (storage.contains(placeholder.getIdentifier())) {
                    storage.getMap().remove(placeholder.getIdentifier());
                }

                try {
                    placeholder.serialize(new File(getPlaceholdersDir(), placeholder.getIdentifier().toLowerCase() + ".yml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

    public IWebPanelHandler getWeb() {
        return web;
    }

    public void setWeb(IWebPanelHandler web) {
        this.web = web;
    }

    public File getPlaceholdersDir() {
        return placeholdersDir;
    }
}
