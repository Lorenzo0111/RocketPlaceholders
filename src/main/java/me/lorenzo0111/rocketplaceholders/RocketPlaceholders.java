package me.lorenzo0111.rocketplaceholders;

import me.lorenzo0111.rocketplaceholders.api.RocketPlaceholdersAPI;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import me.lorenzo0111.rocketplaceholders.creator.placeholders.InternalPlaceholders;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import me.lorenzo0111.rocketplaceholders.updater.UpdateChecker;
import me.lorenzo0111.rocketplaceholders.utilities.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class RocketPlaceholders extends JavaPlugin {

    private static RocketPlaceholdersAPI api;
    private StorageManager storageManager;
    private PluginLoader loader;

    /*

    Plugin by Lorenzo0111 - https://github.com/Lorenzo0111

     */

    public void onEnable() {

        saveDefaultConfig();

        this.storageManager = new StorageManager();

        InternalPlaceholders placeholders = new InternalPlaceholders(this);

        PlaceholdersManager placeholdersManager = new PlaceholdersManager(this.storageManager, placeholders, this);

        api = new RocketPlaceholdersAPI(placeholdersManager);

        placeholders.registerPlaceholders();

        UpdateChecker checker = new UpdateChecker(this, 82678);
        checker.updateCheck();

        loader = new PluginLoader(this, placeholdersManager, checker);
        loader.loadDatabase();

        loader.loadMetrics();
        loader.registerEvents();
        loader.placeholderHook();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled! Thanks for using " + this.getDescription().getName() + " v." + this.getDescription().getVersion());
    }

    @SuppressWarnings("unused")
    public static RocketPlaceholdersAPI getApi() {
        return api;
    }

    public StorageManager getStorageManager() {
        return this.storageManager;
    }

    public PluginLoader getLoader() {
        return this.loader;
    }
}
