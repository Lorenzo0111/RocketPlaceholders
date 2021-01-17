package me.lorenzo0111.rocketplaceholders.storage;

import me.lorenzo0111.rocketplaceholders.creator.PermissionNode;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;

import java.util.HashMap;
import java.util.List;

public class StorageManager {
    private final HashMap<String, Placeholder> hashMap = new HashMap<>();

    public void add(String identifier, Placeholder placeholder) {
        hashMap.put(identifier, placeholder);
    }

    public void build(String identifier, String text) {
        hashMap.put(identifier, new Placeholder(identifier, text));
    }

    public void build(String identifier, String text, List<PermissionNode> permissionNodes) {
        hashMap.put(identifier, new Placeholder(identifier, text, permissionNodes));
    }

    public void clear() {
        hashMap.clear();
    }

    public Placeholder get(String identifier) {
        return hashMap.get(identifier);
    }

    public HashMap<String, Placeholder> getHashMap() {
        return hashMap;
    }
}
