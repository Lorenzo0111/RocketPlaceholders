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

package me.lorenzo0111.rocketplaceholders.storage;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirement;
import me.lorenzo0111.rocketplaceholders.creator.conditions.engine.Requirements;
import me.lorenzo0111.rocketplaceholders.legacy.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class ConfigManager {

    private final RocketPlaceholders plugin;
    private final StorageManager storageManager;

    public ConfigManager(RocketPlaceholders plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
    }

    @SuppressWarnings("deprecation")
    public void registerPlaceholders() {
        final ConfigurationSection config = Objects.requireNonNull(plugin.getConfig().getConfigurationSection("placeholders"), "An error has occurred, configuration error, please contact me on discord (ds.rocketplugins.space)");

        for (String key : config.getKeys(false)) {
            final ConfigurationSection nodesSection = Objects.requireNonNull(config.getConfigurationSection(key)).getConfigurationSection("permissions");
            final ConfigurationSection conditionsSection = Objects.requireNonNull(config.getConfigurationSection(key)).getConfigurationSection("conditions");

            if (conditionsSection == null && nodesSection == null) {
                storageManager.getInternalPlaceholders().build(key, Objects.requireNonNull(config.getString(key + ".placeholder")), ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString(key + ".text"))),null);
            }

            final List<ConditionNode> nodes = new ArrayList<>();

            if (nodesSection != null) {
                nodes.addAll(PermissionNode.createPermissionNodes(plugin, nodesSection));
            }

            if (conditionsSection != null) {
                Requirements requirements = new Requirements(plugin);
                for (String condition : conditionsSection.getKeys(false)) {
                    ConfigurationSection conditionSection = conditionsSection.getConfigurationSection(condition);

                    if (conditionSection !=  null) {
                        Requirement requirement = requirements.parseRequirement(conditionSection);
                        if (requirement != null) {
                            nodes.add(new ConditionNode(requirement,conditionSection.getString("text")));
                        }
                    }

                }

                storageManager.getInternalPlaceholders().build(key, Objects.requireNonNull(config.getString(key + ".placeholder")), ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString(key + ".text"))),nodes);
            }

            if (nodes.isEmpty()) {
                storageManager.getInternalPlaceholders().build(key, Objects.requireNonNull(config.getString(key + ".placeholder")), ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString(key + ".text"))), null);
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
