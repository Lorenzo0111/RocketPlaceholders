package me.Lorenzo0111.RocketPlaceholders;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        ConfigurationSection config = plugin.getConfig().getConfigurationSection("placeholders");

        List<String> changeList = new ArrayList();

        for (String key : config.getKeys(false)) {
            changeList.add(key);
        }

        for(int i = 0; i < changeList.size(); i++) {

            ConfigurationSection getConfig = plugin.getConfig();

            if(identifier.equals(getConfig.getString("placeholders." + i + ".placeholder"))) {
                return plugin.getConfig().getString("placeholders." + i + ".text");
            }
        }
        return null;
    }
}




