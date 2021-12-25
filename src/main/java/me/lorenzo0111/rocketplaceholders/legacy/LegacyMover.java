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

package me.lorenzo0111.rocketplaceholders.legacy;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class LegacyMover {
    private final RocketPlaceholders plugin;
    private final File directory;

    public LegacyMover(RocketPlaceholders plugin, File directory) {
        this.plugin = plugin;
        this.directory = directory;
    }

    @SuppressWarnings("deprecation")
    public void move() throws IOException {
        ConfigurationSection allSection = plugin.getConfig().getConfigurationSection("placeholders");

        if (allSection == null)
            return;

        Set<String> keys = allSection.getKeys(false);

        plugin.getConfig().options().header("All comments have been lost because of the migration.\n If you need help read our documentation at https://docs.rocketplugins.space \n\n Configuration generated with RocketPlaceholders v" + plugin.getDescription().getVersion());
        plugin.getConfig().options().copyHeader(true);
        if (keys.isEmpty()) {
            this.delete(plugin.getConfig());
            return;
        }

        plugin.getLogger().info("Starting migration of placeholders from config.yml..");

        for (String key : keys) {
            plugin.getLogger().info("  | Starting migration of " + allSection.getString(key+".placeholder"));

            File file = new File(directory, allSection.getString(key + ".placeholder") + ".yml");
            if (file.exists() || !file.createNewFile()) {
                plugin.getLogger().warning("Unable to migrate from config.yml to placeholders/" + file.getName() + ".");
                return;
            }

            ConfigurationSection section = allSection.getConfigurationSection(key);
            Objects.requireNonNull(section);

            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection permissions = section.getConfigurationSection("permissions");
            if (permissions != null)
                migratePermissions(permissions,config);
            section.set("permissions",null);

            for (String string : section.getKeys(true))
                migrate(string,section,config);

            config.options().header("Thanks for using RocketPlaceholders v"+plugin.getDescription().getVersion()+".\n" +
                    "\nWe have changed our configuration system to store all placeholders in a better method.\n" +
                    "We want to keep our plugins easy-to-use so we have automatically migrated the placeholder in his file and you don't need to do anything.\n\n" +
                    "If you need support you can contact us on discord: https://discord.io/RocketPlugins");

            config.save(file);
        }

        this.delete(plugin.getConfig());
    }

    private void delete(FileConfiguration file) throws IOException {
        file.set("placeholders",null);
        file.save(new File(plugin.getDataFolder(),"config.yml"));
    }

    private void migrate(String key, ConfigurationSection old, FileConfiguration newFile) {
        if (old.contains(key) && old.get(key) != null && !(old.get(key) instanceof MemorySection))
            newFile.set(key, old.get(key));
    }

    private void migratePermissions(ConfigurationSection old, FileConfiguration config) {
        for (String key : old.getKeys(false)) {
            String path = "conditions.permission-" + key + ".";
            config.set(path+"type","PERMISSION");
            config.set(path+"value",old.getString(key+".permission"));
            config.set(path+"text",old.getString(key+".text"));
        }
    }
}
