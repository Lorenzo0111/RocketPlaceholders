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

package me.lorenzo0111.rocketplaceholders.creator;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.creator.providers.MVdWPlaceholderAPICreator;
import me.lorenzo0111.rocketplaceholders.database.DatabaseManager;
import me.lorenzo0111.rocketplaceholders.storage.ConfigManager;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import me.lorenzo0111.rocketplaceholders.utilities.PluginLoader;
import org.jetbrains.annotations.Nullable;

/**
 * PlaceholdersManager used to handle all placeholders
 */
public class PlaceholdersManager {
    private final StorageManager storageManager;
    private final ConfigManager configManager;
    private final RocketPlaceholders plugin;
    private MVdWPlaceholderAPICreator mVdWPlaceholderAPICreator;

    /**
     * @param storageManager Storage manager that contains all placeholders
     * @param configManager Internal placeholders handler
     * @param plugin JavaPlugin
     */
    public PlaceholdersManager(StorageManager storageManager, ConfigManager configManager, RocketPlaceholders plugin) {
        this.storageManager = storageManager;
        this.configManager = configManager;
        this.plugin = plugin;
    }

    /**
     * Search a placeholder inside the storage manager
     * @param identifier Identifier of the placeholder
     * @return A {@link me.lorenzo0111.rocketplaceholders.creator.Placeholder} or null if not found
     */
    @Nullable
    public Placeholder searchPlaceholder(String identifier) {
        return this.storageManager.get(identifier);
    }

    /**
     * Add a placeholder to the storage manager
     * @param placeholder Placeholder to add
     */
    @Deprecated
    public void addPlaceholder(Placeholder placeholder) {
        this.storageManager.getInternalPlaceholders().add(placeholder.getIdentifier(), placeholder);
    }

    /**
     * Reload the plugin
     */
    public void reload() {
        final PluginLoader loader = this.plugin.getLoader();

        final DatabaseManager databaseManager = loader.getDatabaseManager();

        if (databaseManager != null) {

            if (databaseManager.isMain()) {
                databaseManager.removeAll().thenAccept(bool -> {
                    if (bool) {
                        databaseManager.sync();
                    }

                    databaseManager.reload(configManager);
                });
            } else {
                databaseManager.reload(configManager);
            }

            return;
        }

        this.configManager.reloadPlaceholders();

        if (this.mVdWPlaceholderAPICreator != null) {
            this.mVdWPlaceholderAPICreator.reload();
        }
    }

    /**
     * Hook with MVdWPlaceholderAPI
     */
    public PlaceholdersManager hook(MVdWPlaceholderAPICreator mVdWPlaceholderAPICreator) {
        this.mVdWPlaceholderAPICreator = mVdWPlaceholderAPICreator;
        return this;
    }

    /**
     * @return Storage manager that contains all placeholders
     */
    public StorageManager getStorageManager() {
        return storageManager;
    }
}
