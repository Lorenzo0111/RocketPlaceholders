package me.lorenzo0111.rocketplaceholders.creator.placeholders;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.PermissionNode;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class InternalPlaceholders {

    private final RocketPlaceholders plugin;
    private final StorageManager storageManager;

    public InternalPlaceholders(RocketPlaceholders plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
    }

    public void registerPlaceholders() {
        final ConfigurationSection config = plugin.getConfig().getConfigurationSection("placeholders");

        if (config == null) {
            plugin.getLogger().severe("An error has occurred, configuration error, please contact me on discord (ds.rocketplugins.space)");
            return;
        }

        for (String key : config.getKeys(false)) {
            final ConfigurationSection nodesSection = config.getConfigurationSection(key).getConfigurationSection("permissions");

            if (nodesSection == null) {
                storageManager.getInternalPlaceholders().build(config.getString(key + ".placeholder"), ChatColor.translateAlternateColorCodes('&', config.getString(key + ".text")));
            } else {

                final List<PermissionNode> nodes = new ArrayList<>();
                for (String nodeKey : nodesSection.getKeys(false)) {
                    if (nodesSection.getString(nodeKey + ".permission") != null && nodesSection.getString(nodeKey + ".text") != null) {
                        final PermissionNode node = new PermissionNode(nodesSection.getString(nodeKey + ".permission"), ChatColor.translateAlternateColorCodes('&', nodesSection.getString(nodeKey + ".text")));
                        nodes.add(node);
                    }
                }

                final String identifier = config.getString(key + ".placeholder");
                final String text = config.getString(key + ".text");

                if (identifier != null && text != null) {
                    final Placeholder placeholder = new Placeholder(identifier,text,nodes);
                    storageManager.getInternalPlaceholders().add(identifier, placeholder);
                }
            }
        }

        plugin.getLogger().info("Loaded " + storageManager.getInternalPlaceholders().getMap().size() + " placeholders!");
    }

    public void reloadPlaceholders() {
        storageManager.getInternalPlaceholders().clear();

        if (plugin.getLoader().getDatabaseManager() == null || !plugin.getLoader().getDatabaseManager().isMain()) {
            registerPlaceholders();
        }
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
}
