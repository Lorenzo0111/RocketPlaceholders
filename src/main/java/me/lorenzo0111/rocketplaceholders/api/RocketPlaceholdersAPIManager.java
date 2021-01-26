package me.lorenzo0111.rocketplaceholders.api;

import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import org.jetbrains.annotations.Nullable;

public class RocketPlaceholdersAPIManager implements RocketPlaceholdersAPI {
    private final PlaceholdersManager placeholdersManager;

    public RocketPlaceholdersAPIManager(PlaceholdersManager placeholdersManager) {
        this.placeholdersManager = placeholdersManager;
    }

    @Override
    public void addPlaceholder(Placeholder placeholder) {
        this.getInternalPlaceholders().getStorageManager().getExternalPlaceholders().add(placeholder.getIdentifier(), placeholder);
    }

    @Override
    public @Nullable Placeholder getPlaceholder(String identifier) {
        return this.getInternalPlaceholders().searchPlaceholder(identifier);
    }

    @Override
    public PlaceholdersManager getInternalPlaceholders() {
        return placeholdersManager;
    }
}
