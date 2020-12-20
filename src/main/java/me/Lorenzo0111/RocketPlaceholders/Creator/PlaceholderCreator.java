package me.Lorenzo0111.RocketPlaceholders.Creator;


import me.Lorenzo0111.RocketPlaceholders.RocketPlaceholders;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class PlaceholderCreator extends PlaceholderExpansion {

    /*

    Plugin by Lorenzo0111

     */

    private final RocketPlaceholders plugin;
    private final InternalPlaceholders internalPlaceholders;

    public PlaceholderCreator(RocketPlaceholders plugin, InternalPlaceholders internalPlaceholders) {
        this.plugin = plugin;
        this.internalPlaceholders = internalPlaceholders;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "rp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Lorenzo0111";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {

        if (player == null) {
            return null;
        }

        if (!player.isOnline()) {
            return null;
        }

        Player onlinePlayer = player.getPlayer();

        Placeholder placeholder = internalPlaceholders.searchInternalPlaceholder(identifier);

        if (placeholder == null) {
            return "";
        }

        if (placeholder.getPermission() == null) {
            return placeholder.getText();
        }

        if (onlinePlayer.hasPermission(placeholder.getPermission())) {
            return placeholder.getPermission_text();
        } else {
            return placeholder.getText();
        }

    }
}