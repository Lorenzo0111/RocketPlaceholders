package me.Lorenzo0111.RocketPlaceholders;

import me.Lorenzo0111.RocketPlaceholders.Updater.UpdateChecker;
import me.Lorenzo0111.RocketPlaceholders.Utilities.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class RocketPlaceholders extends JavaPlugin {

    /*

    Plugin by Lorenzo0111 - https://github.com/Lorenzo0111

     */

    public void onEnable() {

        PluginLoader loader = new PluginLoader(this);
        loader.placeholderHook();
        loader.loadMetrics();
        loader.registerEvents();

        UpdateChecker checker = new UpdateChecker(this, 82678);
        checker.updateCheck();

        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled! Thanks for using " + this.getDescription().getName() + " v." + this.getDescription().getVersion());
    }
}
