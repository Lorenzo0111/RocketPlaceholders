package me.lorenzo0111.rocketplaceholders.creator;

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

    public Placeholder(String identifier, String text, List<PermissionNode> permissionNodes) {
        this.identifier = identifier;
        this.text = text;
        this.permissionNodes = permissionNodes;
    }

    public Placeholder(String identifier, String text) {
        this.identifier = identifier;
        this.text = text;
    }


    public String getIdentifier() {
        return identifier;
    }

    public String getText() {
        return text;
    }

    public List<PermissionNode> getPermissionNodes() {
        return permissionNodes;
    }

    public boolean hasPermissionNodes() {
        return permissionNodes != null;
    }
}
