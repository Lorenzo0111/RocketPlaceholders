package me.lorenzo0111.rocketplaceholders.api;

import me.lorenzo0111.rocketplaceholders.creator.PermissionNode;
import me.lorenzo0111.rocketplaceholders.creator.Placeholder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PlaceholderBuilder {
    private final String identifier;
    private final String text;
    private final List<PermissionNode> permissionNodes = new ArrayList<>();

    public PlaceholderBuilder(String identifier, String text) {
        this.identifier = identifier;
        this.text = text;
    }

    public PlaceholderBuilder createPermissionNode(String permission, String text) {
        permissionNodes.add(new PermissionNode(permission, text));

        return this;
    }

    public Placeholder build() {
        return new Placeholder(identifier, text, permissionNodes);
    }
}
