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
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.conditions.ConditionNode;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirement;
import me.lorenzo0111.rocketplaceholders.creator.conditions.Requirements;
import me.lorenzo0111.rocketplaceholders.exceptions.InvalidConditionException;
import me.lorenzo0111.rocketplaceholders.legacy.FileMover;
import me.lorenzo0111.rocketplaceholders.legacy.PlaceholderSplitter;
import me.lorenzo0111.rocketplaceholders.utilities.HexParser;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class ConfigManager {

    private final RocketPlaceholders plugin;
    private final StorageManager storageManager;

    public ConfigManager(RocketPlaceholders plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
    }

    public void registerPlaceholders() throws IOException, InvalidConditionException {
        File dir = new File(plugin.getDataFolder(), "placeholders");
        if (!dir.exists() && dir.mkdirs()) {
            File example = new File(dir, "example.yml");

            if (!example.exists()) {
                try (InputStream in = RocketPlaceholders.class.getClassLoader().getResourceAsStream( "example.yml" )) {
                    Objects.requireNonNull(in);

                    Files.copy(in, example.toPath());
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "Unable to create example configuration", e);
                }
            }
        }

        FileMover mover = new FileMover(plugin,dir);
        if (mover.shouldMigrate())
            mover.migrate();

        File[] files = dir.listFiles(new PlaceholderFilter());

        Objects.requireNonNull(files, "An error has occurred while loading placeholders files.");

        for (File file : files) {
            PlaceholderSplitter splitter = new PlaceholderSplitter(file);
            if (splitter.shouldMigrate())
                splitter.migrate();

            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

            for (String key : configuration.getConfigurationSection("placeholders").getKeys(false)) {
                ConfigurationSection config = configuration.getConfigurationSection("placeholders." + key);
                if (config == null || !config.getString("placeholder", "").equalsIgnoreCase(key)) {
                    plugin.getLogger().severe("Placeholder key must match with the identifier. Skipping " + key);
                    continue;
                }

                ConfigurationSection conditions = config.getConfigurationSection("conditions");

                final boolean parseJS = config.getBoolean("parsejs");

                List<ConditionNode> nodes = null;
                if (conditions != null) {
                    nodes = new ArrayList<>(scanConditions(conditions));
                }

                storageManager.getInternalPlaceholders().build(FilenameUtils.getBaseName(file.getName()), config.getString("placeholder", "null"), HexParser.text(config.getString("text", "")),nodes,parseJS);
            }
        }

        plugin.getLogger().info("Loaded " + storageManager.getInternalPlaceholders().getMap().size() + " placeholders!");
    }

    public static List<ConditionNode> scanConditions(ConfigurationSection section) throws InvalidConditionException {
        List<ConditionNode> nodes = new ArrayList<>();

        for (String condition : section.getKeys(false)) {
            ConfigurationSection conditionSection = section.getConfigurationSection(condition);

            if (conditionSection !=  null) {
                Requirement requirement = Requirements.parseRequirement(conditionSection);
                if (requirement != null) {
                    nodes.add(new ConditionNode(requirement,conditionSection.getString("text")));
                }
            }

        }

        return nodes;
    }

    public void reloadPlaceholders() throws IOException {
        storageManager.getInternalPlaceholders().clear();

        if (plugin.getLoader().getDatabaseManager() == null || !plugin.getLoader().getDatabaseManager().isMain()) {
            registerPlaceholders();
        }
    }

    public void reload(@Nullable String oldIdentifier, @NotNull Placeholder placeholder) {
        FileConfiguration config = placeholder.getConfig();

        ConfigurationSection conditions = config.getConfigurationSection("conditions");

        final boolean parseJS = config.getBoolean("parsejs");

        if (conditions != null) {
            final List<ConditionNode> nodes = new ArrayList<>(scanConditions(conditions));

            storageManager.getInternalPlaceholders()
                            .getMap()
                            .remove(oldIdentifier == null ? placeholder.getIdentifier() : oldIdentifier);
            storageManager.getInternalPlaceholders().build(placeholder.getFile().getName(), config.getString("placeholder", "null"), HexParser.text(config.getString("text", "")),nodes.isEmpty() ? null : nodes,parseJS);
        }
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
}
