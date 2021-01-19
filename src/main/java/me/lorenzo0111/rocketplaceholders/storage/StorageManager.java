package me.lorenzo0111.rocketplaceholders.storage;

import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import org.jetbrains.annotations.Nullable;

public class StorageManager {
    private final Storage internalPlaceholders = new Storage();
    private final Storage externalPlaceholders = new Storage();

    public Storage getInternalPlaceholders() {
        return internalPlaceholders;
    }

    public Storage getExternalPlaceholders() {
        return externalPlaceholders;
    }

    @Nullable
    public Placeholder get(String identifier) {
        if (internalPlaceholders.getHashMap().containsKey(identifier)) {
            return internalPlaceholders.get(identifier);
        }

        return externalPlaceholders.get(identifier);
    }
}
