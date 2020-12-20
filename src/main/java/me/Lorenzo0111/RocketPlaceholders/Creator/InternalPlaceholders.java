package me.Lorenzo0111.RocketPlaceholders.Creator;

import me.Lorenzo0111.RocketPlaceholders.RocketPlaceholders;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class InternalPlaceholders {

    private final RocketPlaceholders plugin;
    private final HashMap<String,Placeholder> internalPlaceholders = new HashMap<>();

    public InternalPlaceholders(RocketPlaceholders plugin) {
        this.plugin = plugin;
    }

    public HashMap<String,Placeholder> getInternalPlaceholders() {
        return internalPlaceholders;
    }

    public Placeholder searchInternalPlaceholder(String identififer) {
        return internalPlaceholders.get(identififer);
    }

    public void registerPlaceholders() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("placeholders");

        for (String key : config.getKeys(false)) {
            if (config.getString(key + ".permission") == null) {
                internalPlaceholders.put(config.getString(key + ".placeholder"), new Placeholder(config.getString(key + ".placeholder"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text"))));
            } else if (config.getString(key + ".text_with_permission") == null) {
                internalPlaceholders.put(config.getString(key + ".placeholder"), new Placeholder(config.getString(key + ".placeholder"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text"))));
            } else {
                internalPlaceholders.put(config.getString(key + ".placeholder"), new Placeholder(config.getString(key + ".placeholder"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text")), config.getString(key + ".permission"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text_with_permission"))));
            }
        }

        plugin.getLogger().info("Loaded " + internalPlaceholders.size() + " placeholders!");
    }

    public void reloadPlaceholders() {
        internalPlaceholders.clear();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("placeholders");

        for (String key : config.getKeys(false)) {
            if (config.getString(key + ".permission") == null) {
                internalPlaceholders.put(config.getString(key + ".placeholder"), new Placeholder(config.getString(key + ".placeholder"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text"))));
            } else if (config.getString(key + ".text_with_permission") == null) {
                internalPlaceholders.put(config.getString(key + ".placeholder"), new Placeholder(config.getString(key + ".placeholder"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text"))));
            } else {
                internalPlaceholders.put(config.getString(key + ".placeholder"), new Placeholder(config.getString(key + ".placeholder"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text")), config.getString(key + ".permission"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text_with_permission"))));
            }
        }

        plugin.getLogger().info("Loaded " + internalPlaceholders.size() + " placeholders!");

    }
}
