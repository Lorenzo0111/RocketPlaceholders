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

        } else {
            this.internalPlaceholders.reloadPlaceholders();
        }

    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
}
