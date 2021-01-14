package me.Lorenzo0111.RocketPlaceholders.Creator;

import me.Lorenzo0111.RocketPlaceholders.RocketPlaceholders;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InternalPlaceholders {

    private final RocketPlaceholders plugin;
    private final HashMap<String,Placeholder> internalPlaceholders = new HashMap<>();

    public InternalPlaceholders(RocketPlaceholders plugin) {
        this.plugin = plugin;
    }

    public HashMap<String,Placeholder> getInternalPlaceholders() {
        return internalPlaceholders;
    }

    public Placeholder searchInternalPlaceholder(String identifier) {
        return internalPlaceholders.get(identifier);
    }

    public void registerPlaceholders() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("placeholders");

        for (String key : config.getKeys(false)) {
            ConfigurationSection nodesSection = config.getConfigurationSection(key).getConfigurationSection("permissions");

            if (nodesSection == null) {
                internalPlaceholders.put(config.getString(key + ".placeholder"), new Placeholder(config.getString(key + ".placeholder"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text"))));
            } else {

                List<PermissionNode> nodes = new ArrayList<>();
                for (String nodeKey : nodesSection.getKeys(false)) {
                    if (nodesSection.getString(nodeKey + ".permission") != null && nodesSection.getString(nodeKey + ".text") != null) {
                        PermissionNode node = new PermissionNode(nodesSection.getString(nodeKey + ".permission"), nodesSection.getString(nodeKey + ".text"));
                        nodes.add(node);
                    }
                }
                Placeholder placeholder = new Placeholder(config.getString(key + ".placeholder"),config.getString(key + ".text"),nodes);
                internalPlaceholders.put(config.getString(key + ".placeholder"), placeholder);
            }
        }

        plugin.getLogger().info("Loaded " + internalPlaceholders.size() + " placeholders!");
    }

    public void reloadPlaceholders() {
        internalPlaceholders.clear();
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("placeholders");

        for (String key : config.getKeys(false)) {
            ConfigurationSection nodesSection = config.getConfigurationSection(key).getConfigurationSection("permissions");

            if (nodesSection == null) {
                internalPlaceholders.put(config.getString(key + ".placeholder"), new Placeholder(config.getString(key + ".placeholder"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text"))));
            } else {

                List<PermissionNode> nodes = new ArrayList<>();
                for (String nodeKey : nodesSection.getKeys(false)) {
                    if (nodesSection.getString(nodeKey + ".permission") != null && nodesSection.getString(nodeKey + ".text") != null) {
                        PermissionNode node = new PermissionNode(nodesSection.getString(nodeKey + ".permission"), nodesSection.getString(nodeKey + ".text"));
                        nodes.add(node);
                    }
                }
                Placeholder placeholder = new Placeholder(config.getString(key + ".placeholder"),config.getString(key + ".text"),nodes);
                internalPlaceholders.put(config.getString(key + ".placeholder"), placeholder);
            }
        }

        plugin.getLogger().info("Loaded " + internalPlaceholders.size() + " placeholders!");

    }
}
