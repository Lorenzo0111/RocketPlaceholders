package me.lorenzo0111.rocketplaceholders.storage;

import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import org.jetbrains.annotations.Nullable;

public class StorageManager {
    private final Storage internalPlaceholders = new Storage();
    private final Storage externalPlaceholders = new Storage();

    public Storage getInternalPlaceholders() {
        return this.internalPlaceholders;
    }

    public Storage getExternalPlaceholders() {
        return this.externalPlaceholders;
    }

    @Nullable
    public Placeholder get(String identifier) {
        if (this.internalPlaceholders.getHashMap().containsKey(identifier)) {
            return this.internalPlaceholders.get(identifier);
        }

        return this.externalPlaceholders.get(identifier);
    }
}
