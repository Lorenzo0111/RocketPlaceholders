package me.lorenzo0111.rocketplaceholders.storage;

import me.lorenzo0111.rocketplaceholders.creator.PermissionNode;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {
    private final Map<String, Placeholder> placeholders = new HashMap<>();

    public void add(@NotNull String identifier, @NotNull Placeholder placeholder) {
        this.placeholders.put(identifier, placeholder);
    }

    public void build(@NotNull String identifier, @NotNull String text) {
        this.placeholders.put(identifier, new Placeholder(identifier, text));
    }

    public void build(@NotNull String identifier, @NotNull String text, List<PermissionNode> permissionNodes) {
        this.placeholders.put(identifier, new Placeholder(identifier, text, permissionNodes));
    }

    public void clear() {
        this.placeholders.clear();
    }

    @Nullable
    public Placeholder get(String identifier) {
        return this.placeholders.get(identifier);
    }

    public Map<String, Placeholder> getMap() {
        return this.placeholders;
    }
}
