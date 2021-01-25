package me.lorenzo0111.rocketplaceholders.utilities;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.logging.Logger;

public class Debugger {

    private final RocketPlaceholders plugin;
    private final Logger logger;

    public Debugger(RocketPlaceholders plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public void debug() {
        this.log("-----------[ RocketPlugins Debugger ]-----------");

        this.log("Server Information:");
        this.logData("Server Version", Bukkit.getServer().getBukkitVersion());
        this.logData("Server Software", Bukkit.getServer().getVersion());
        this.logData("Server Plugins", Arrays.toString(Bukkit.getServer().getPluginManager().getPlugins()));

        this.log("");

        this.log("Plugin Information");
        this.logData("Plugin Name", plugin.getDescription().getName());
        this.logData("Plugin Version", plugin.getDescription().getVersion());

        this.log("-----------[ RocketPlugins Debugger ]-----------");
    }

    private void logData(String prefix, String message) {
        this.log(prefix + ": " + message);
    }

    private void log(String message) {
        logger.info(message);
    }

}
