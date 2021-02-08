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
import me.lorenzo0111.rocketplaceholders.creator.placeholders.InternalPlaceholders;
import me.lorenzo0111.rocketplaceholders.database.DatabaseManager;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import me.lorenzo0111.rocketplaceholders.utilities.PluginLoader;
import org.jetbrains.annotations.Nullable;

public class PlaceholdersManager {
    private final StorageManager storageManager;
    private final InternalPlaceholders internalPlaceholders;
    private final RocketPlaceholders plugin;

    public PlaceholdersManager(StorageManager storageManager, InternalPlaceholders internalPlaceholders, RocketPlaceholders plugin) {
        this.storageManager = storageManager;
        this.internalPlaceholders = internalPlaceholders;
        this.plugin = plugin;
    }

    @Nullable
    public Placeholder searchPlaceholder(String identifier) {
        return this.storageManager.get(identifier);
    }

    public void addPlaceholder(Placeholder placeholder) {
        this.storageManager.getInternalPlaceholders().add(placeholder.getIdentifier(), placeholder);
    }

    public void reload() {
        final PluginLoader loader = this.plugin.getLoader();

        final DatabaseManager databaseManager = loader.getDatabaseManager();

        if (databaseManager != null) {

            if (databaseManager.isMain()) {
                databaseManager.removeAll().thenAccept(bool -> {
                    if (bool) {
                        databaseManager.sync();
                    }

                    databaseManager.reload(internalPlaceholders);
                });
            } else {
                databaseManager.reload(internalPlaceholders);
            }

            return;
        }

        this.internalPlaceholders.reloadPlaceholders();

    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
}
