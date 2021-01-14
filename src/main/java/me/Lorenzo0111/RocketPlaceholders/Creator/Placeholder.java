package me.Lorenzo0111.RocketPlaceholders.Creator;

import java.util.List;

public class Placeholder {

    private final String identififer;
    private final String text;
    private List<PermissionNode> permissionNodes;

    public Placeholder(String identifier, String text, List<PermissionNode> permissionNodes) {
        this.identififer = identifier;
        this.text = text;
        this.permissionNodes = permissionNodes;
    }

    public Placeholder(String identifier, String text) {
        this.identififer = identifier;
        this.text = text;
    }


    public String getIdentifier() {
        return identififer;
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
