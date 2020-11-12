package me.Lorenzo0111.RocketPlaceholders;

import me.Lorenzo0111.RocketPlaceholders.Command.MainCommand;
import me.Lorenzo0111.RocketPlaceholders.Creator.PlaceholderCreator;
import me.Lorenzo0111.RocketPlaceholders.Listener.Join;
import me.Lorenzo0111.RocketPlaceholders.Updater.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Placeholders extends JavaPlugin {

    /*

    Plugin by Lorenzo0111 - https://github.com/Lorenzo0111

     */

    public Integer code = 82678;

    public void onEnable() {
        new UpdateChecker(this, code).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("There is not a new update available.");
            } else {
                getLogger().info("There is a new update available. Download it from: https://bit.ly/RocketJoin");
            }
        });

        saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(new Join(this), this);
        this.getCommand("rocketplaceholders").setExecutor(new MainCommand(this));
        this.getCommand("rocketplaceholders").setTabCompleter(new MainCommand(this));

        new Metrics(this, 9381);

        if (Bukkit.getPluginManager().getPlugin("RocketJoin") != null) {
            getLogger().info("Found RocketJoin, thanks for using RocketPlugins!");
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("PlaceholderAPI hooked!");
            getLogger().info(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " by Lorenzo0111 is now enabled!");
            new PlaceholderCreator(this).register();
            return;
        }

        getLogger().severe("Could not find PlaceholderAPI! This plugin is required.");
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled! Thanks for using " + this.getDescription().getName() + " v." + this.getDescription().getVersion());
    }
}
