package me.lorenzo0111.rocketplaceholders.creator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Placeholder {

    private final String identifier;
    private final String text;
    private List<PermissionNode> permissionNodes;

    @Override
    public String toString() {
        return "Placeholder{" +
                "identifier='" + identifier + '\'' +
                ", text='" + text + '\'' +
                ", permissionNodes=" + permissionNodes +
                '}';
    }

    public Placeholder(@NotNull String identifier, @NotNull String text, List<PermissionNode> permissionNodes) {
        this.identifier = identifier;
        this.text = text;
        this.permissionNodes = permissionNodes;
    }

    public Placeholder(@NotNull String identifier, @NotNull String text) {
        this.identifier = identifier;
        this.text = text;
    }


    public String getIdentifier() {
        return this.identifier;
    }

    public String getText() {
        return this.text;
    }

    public List<PermissionNode> getPermissionNodes() {
        return this.permissionNodes;
    }

    public boolean hasPermissionNodes() {
        return this.permissionNodes != null;
    }
}
