package me.lorenzo0111.rocketplaceholders;

import me.lorenzo0111.rocketplaceholders.creator.InternalPlaceholders;
import me.lorenzo0111.rocketplaceholders.updater.UpdateChecker;
import me.lorenzo0111.rocketplaceholders.utilities.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class RocketPlaceholders extends JavaPlugin {

    /*

    Plugin by Lorenzo0111 - https://github.com/Lorenzo0111

     */

    public void onEnable() {

        saveDefaultConfig();

        InternalPlaceholders placeholders = new InternalPlaceholders(this);
        placeholders.registerPlaceholders();

        UpdateChecker checker = new UpdateChecker(this, 82678);
        checker.updateCheck();

        PluginLoader loader = new PluginLoader(this, placeholders, checker);
        loader.placeholderHook();
        loader.loadMetrics();
        loader.registerEvents();

    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled! Thanks for using " + this.getDescription().getName() + " v." + this.getDescription().getVersion());
    }
}
