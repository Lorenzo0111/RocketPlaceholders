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
        log("-----------[ RocketPlugins Debugger ]-----------");

        log("Server Information:");
        logData("Server Version", Bukkit.getServer().getBukkitVersion());
        logData("Server Software", Bukkit.getServer().getVersion());
        logData("Server Plugins", Arrays.toString(Bukkit.getServer().getPluginManager().getPlugins()));

        log("");

        log("Plugin Information");
        logData("Plugin Name", plugin.getDescription().getName());
        logData("Plugin Version", plugin.getDescription().getVersion());

        log("-----------[ RocketPlugins Debugger ]-----------");
    }

    private void logData(String prefix, String message) {
        log(prefix + ": " + message);
    }

    private void log(String message) {
        logger.info(message);
    }

}
