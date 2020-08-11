package me.Lorenzo0111.RocketPlaceholders;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

public class PlaceholderCreator extends PlaceholderExpansion {

    private Placeholders plugin;

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
        return "1.0";
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public boolean persist(){
        return true;
    }

    public PlaceholderCreator(Placeholders plugin){
        this.plugin = plugin;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){

        // Placeholder 1
        if(identifier.equals(plugin.getConfig().getString("placeholders.1.placeholder"))){
            return plugin.getConfig().getString("placeholders.1.text");
        }

        // Placeholder 2
        if(identifier.equals(plugin.getConfig().getString("placeholders.2.placeholder"))){
            return plugin.getConfig().getString("placeholders.2.text");
        }

        if(identifier.equals(plugin.getConfig().getString("placeholders.3.placeholder"))){
            return plugin.getConfig().getString("placeholders.3.text");
        }

        if(identifier.equals(plugin.getConfig().getString("placeholders.4.placeholder"))){
            return plugin.getConfig().getString("placeholders.4.text");
        }

        if(identifier.equals(plugin.getConfig().getString("placeholders.5.placeholder"))){
            return plugin.getConfig().getString("placeholders.5.text");
        }

        if(identifier.equals(plugin.getConfig().getString("placeholders.6.placeholder"))){
            return plugin.getConfig().getString("placeholders.6.text");
        }

        if(identifier.equals(plugin.getConfig().getString("placeholders.7.placeholder"))){
            return plugin.getConfig().getString("placeholders.7.text");
        }

        if(identifier.equals(plugin.getConfig().getString("placeholders.8.placeholder"))){
            return plugin.getConfig().getString("placeholders.8.text");
        }

        if(identifier.equals(plugin.getConfig().getString("placeholders.9.placeholder"))){
            return plugin.getConfig().getString("placeholders.9.text");
        }


        return null;
    }
}
