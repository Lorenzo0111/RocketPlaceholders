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
        if(identifier.equals(plugin.getConfig().getString("placeholders.placeholder1"))){
            return plugin.getConfig().getString("placeholders.text1");
        }

        // Placeholder 2
        if(identifier.equals(plugin.getConfig().getString("placeholders.placeholder2"))){
            return plugin.getConfig().getString("placeholders.text2");
        }
        return null;
    }
}
