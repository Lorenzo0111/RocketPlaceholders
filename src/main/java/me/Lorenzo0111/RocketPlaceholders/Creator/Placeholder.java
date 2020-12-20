package me.Lorenzo0111.RocketPlaceholders.Creator;

import org.bukkit.permissions.Permission;

public class Placeholder {

    private String identififer;
    private String text;
    private Permission permission;
    private String permissiontext;

    public Placeholder(String identifier, String text, String permission, String permissiontext) {
        this.identififer = identifier;
        this.text = text;
        this.permission = new Permission(permission);
        this.permissiontext = permissiontext;
    }

    public Placeholder(String identifier, String text, Permission permission, String permissiontext) {
        this.identififer = identifier;
        this.text = text;
        this.permission = permission;
        this.permissiontext = permissiontext;
    }

    public Placeholder(String identifier, String text) {
        this.identififer = identifier;
        this.text = text;
    }

    public void setIdentififer(String identififer) {
        this.identififer = identififer;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void setPermission(String permission) {
        this.permission = new Permission(permission);
    }

    public void setPermissionText(String permission_text) {
        this.permissiontext = permission_text;
    }


    public String getIdentififer() {
        return identififer;
    }

    public String getText() {
        return text;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getPermission_text() {
        return permissiontext;
    }
}
