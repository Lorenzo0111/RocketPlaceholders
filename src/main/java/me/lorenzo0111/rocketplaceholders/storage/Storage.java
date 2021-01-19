package me.lorenzo0111.rocketplaceholders.storage;

import me.lorenzo0111.rocketplaceholders.creator.PermissionNode;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;

import java.util.HashMap;
import java.util.List;

public class Storage {
    private final HashMap<String, Placeholder> placeholders = new HashMap<>();

    public void add(String identifier, Placeholder placeholder) {
        placeholders.put(identifier, placeholder);
    }

    public void build(String identifier, String text) {
        placeholders.put(identifier, new Placeholder(identifier, text));
    }

    public void build(String identifier, String text, List<PermissionNode> permissionNodes) {
        placeholders.put(identifier, new Placeholder(identifier, text, permissionNodes));
    }

    public void clear() {
        placeholders.clear();
    }

    public Placeholder get(String identifier) {
        return placeholders.get(identifier);
    }

    public HashMap<String, Placeholder> getHashMap() {
        return placeholders;
    }
}
