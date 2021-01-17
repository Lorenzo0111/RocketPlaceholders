package me.lorenzo0111.rocketplaceholders.creator;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class InternalPlaceholders {

    private final RocketPlaceholders plugin;
    private final StorageManager storageManager = new StorageManager();

    public InternalPlaceholders(RocketPlaceholders plugin) {
        this.plugin = plugin;
    }

    public Placeholder searchInternalPlaceholder(String identifier) {
        return storageManager.get(identifier);
    }

    public void registerPlaceholders() {
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("placeholders");

        if (config == null) {
            plugin.getLogger().severe("An error has occurred, configuration error, please contact me on discord (ds.rocketplugins.space)");
            return;
        }

        for (String key : config.getKeys(false)) {
            ConfigurationSection nodesSection = config.getConfigurationSection(key).getConfigurationSection("permissions");

            if (nodesSection == null) {
                storageManager.build(config.getString(key + ".placeholder"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text")));
            } else {

                List<PermissionNode> nodes = new ArrayList<>();
                for (String nodeKey : nodesSection.getKeys(false)) {
                    if (nodesSection.getString(nodeKey + ".permission") != null && nodesSection.getString(nodeKey + ".text") != null) {
                        PermissionNode node = new PermissionNode(nodesSection.getString(nodeKey + ".permission"), ChatColor.translateAlternateColorCodes('&', nodesSection.getString(nodeKey + ".text")));
                        nodes.add(node);
                    }
                }
                Placeholder placeholder = new Placeholder(config.getString(key + ".placeholder"),config.getString(key + ".text"),nodes);
                storageManager.add(config.getString(key + ".placeholder"), placeholder);
            }
        }

        plugin.getLogger().info("Loaded " + storageManager.getHashMap().size() + " placeholders!");
    }

    public void reloadPlaceholders() {
        storageManager.clear();

        registerPlaceholders();
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
}
