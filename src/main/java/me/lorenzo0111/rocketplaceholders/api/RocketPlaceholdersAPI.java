package me.lorenzo0111.rocketplaceholders.api;

import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class RocketPlaceholdersAPI {
    public final PlaceholdersManager placeholdersManager;

    public RocketPlaceholdersAPI(PlaceholdersManager placeholdersManager) {
        this.placeholdersManager = placeholdersManager;
    }

    public void addPlaceholder(Placeholder placeholder) {
        placeholdersManager.getStorageManager().getExternalPlaceholders().add(placeholder.getIdentifier(), placeholder);
    }

    @Nullable
    public Placeholder getPlaceholder(String identifier) {
       return placeholdersManager.searchPlaceholder(identifier);
    }

    public PlaceholdersManager getInternalPlaceholders() {
        return placeholdersManager;
    }
}
