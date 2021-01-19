package me.lorenzo0111.rocketplaceholders.creator;
import me.lorenzo0111.rocketplaceholders.creator.placeholders.InternalPlaceholders;
import me.lorenzo0111.rocketplaceholders.storage.StorageManager;
import org.jetbrains.annotations.Nullable;

public class PlaceholdersManager {
    private final StorageManager storageManager;

    private final InternalPlaceholders internalPlaceholders;

    public PlaceholdersManager(StorageManager storageManager, InternalPlaceholders internalPlaceholders) {
        this.storageManager = storageManager;
        this.internalPlaceholders = internalPlaceholders;
    }

    @Nullable
    public Placeholder searchPlaceholder(String identifier) {
        return this.storageManager.get(identifier);
    }

    public void addPlaceholder(Placeholder placeholder) {
        this.storageManager.getInternalPlaceholders().add(placeholder.getIdentifier(), placeholder);
    }

    public void reload() {
        this.internalPlaceholders.reloadPlaceholders();
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
}
