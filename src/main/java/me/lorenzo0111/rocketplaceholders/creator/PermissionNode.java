package me.lorenzo0111.rocketplaceholders.creator;

public class PermissionNode {
    private final String permission;
    private final String text;

    public PermissionNode(String permission, String text) {
        this.permission = permission;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return "PermissionNode{" +
                "permission='" + permission + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

}
