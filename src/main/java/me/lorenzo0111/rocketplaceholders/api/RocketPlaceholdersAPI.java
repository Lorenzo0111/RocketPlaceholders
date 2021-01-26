package me.lorenzo0111.rocketplaceholders.api;

import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import me.lorenzo0111.rocketplaceholders.creator.PlaceholdersManager;
import org.jetbrains.annotations.Nullable;

public interface RocketPlaceholdersAPI {

    void addPlaceholder(Placeholder placeholder);

    @Nullable
    Placeholder getPlaceholder(String identifier);

    PlaceholdersManager getInternalPlaceholders();
}
