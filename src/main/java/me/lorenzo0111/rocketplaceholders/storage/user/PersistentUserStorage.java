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

package me.lorenzo0111.rocketplaceholders.storage.user;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersistentUserStorage extends VolatileUserStorage {
    private final File file;

    public PersistentUserStorage(@NotNull File file) {
        this.file = file;

        ConfigurationSerialization.registerClass(UserText.class);

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    return;
                }
            } catch (IOException ignored) {}

            RocketPlaceholders.getInstance().getLogger().severe("An error has occurred while creating the user storage file!");
        }
    }

    @Override
    public boolean persistent() {
        return true;
    }

    @Override
    public void save() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("storage", storage);
        try {
            config.save(file);
        } catch (IOException e) {
            RocketPlaceholders.getInstance().getLogger().severe("An error has occurred while saving the user storage file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void load() {
        ConfigurationSection config = YamlConfiguration.loadConfiguration(file);
        this.storage.clear();
        this.storage.addAll((List<UserText>) config.getList("storage", new ArrayList<>()));
    }

}
