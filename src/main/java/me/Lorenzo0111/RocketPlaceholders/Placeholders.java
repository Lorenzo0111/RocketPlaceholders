package me.Lorenzo0111.RocketPlaceholders;

import me.Lorenzo0111.RocketPlaceholders.Updater.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Placeholders extends JavaPlugin implements Listener {

    /*

    Plugin by Lorenzo0111


     */

    public Integer code = getConfig().getInt("resource-id");

    public void onEnable() {
        Logger logger = this.getLogger();
        new UpdateChecker(this, code).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("There is not a new update available.");
            } else {
                logger.info("There is a new update available.");
                logger.info("Download it from: https://bit.ly/RocketPlaceholders");
            }
        });
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            logger.info("Plugin enabled! Thanks for using " + this.getDescription().getName() + " v." + this.getDescription().getVersion());
            Bukkit.getPluginManager().registerEvents(this, (Plugin) this);
            new PlaceholderCreator(this).register();
            saveDefaultConfig();
        } else {
            logger.warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled! Thanks for using " + this.getDescription().getName() + " v." + this.getDescription().getVersion());
    }

    //  Command(Info and reload)
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rocketplaceholders")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("rocketplaceholders.command")) {
                    if (args.length == 0) {
                        sender.sendMessage("%prefix%&r &7Plugin by &eLorenzo0111&7!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                        sender.sendMessage("%prefix%&r &7Use &8/rocketplaceholders reload &7to reload the plugin!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            reloadConfig();
                            Logger logger = this.getLogger();
                            new UpdateChecker(this, code).getVersion(version -> {
                                if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                                    logger.info("There is not a new update available.");
                                } else {
                                    logger.info("There is a new update available.");
                                    logger.info("Download it from: https://bit.ly/RocketPlaceholders");                                }
                            });
                            sender.sendMessage("%prefix%&r &7Plugin reloaded!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                        } else {
                            sender.sendMessage("%prefix%&r &7Use &8/rocketplaceholders reload &7to reload the plugin!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                        }
                    } else {
                        sender.sendMessage("%prefix%&r &7Plugin by &eLorenzo0111&7!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                        sender.sendMessage("%prefix%&r &7Use &8/rocketplaceholders reload &7to reload the plugin!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                    }
                } else {
                    sender.sendMessage(getConfig().getString("prefix").replace("&", "§") + " " + getConfig().getString("no_permission").replace("&", "§"));
                }
            } else {
                if (args.length == 0) {
                    sender.sendMessage("%prefix%&r &7Plugin by &eLorenzo0111&7!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                    sender.sendMessage("%prefix%&r &7Use &8/rocketplaceholders reload &7to reload the plugin!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        reloadConfig();
                        Logger logger = this.getLogger();
                        new UpdateChecker(this, code).getVersion(version -> {
                            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                                logger.info("There is not a new update available.");
                            } else {
                                logger.info("There is a new update available.");
                                logger.info("Download it from: https://bit.ly/RocketPlaceholders");                            }
                        });
                        sender.sendMessage("%prefix%&r &7Plugin reloaded!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                    } else {
                        sender.sendMessage("%prefix%&r &7Use &8/rocketplaceholders reload &7to reload the plugin!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                    }
                } else {
                    sender.sendMessage("%prefix%&r &7Plugin by &eLorenzo0111&7!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                    sender.sendMessage("%prefix%&r &7Use &8/rocketplaceholders reload &7to reload the plugin!".replace("%prefix%", getConfig().getString("prefix")).replace("&", "§"));
                }
            }
        } return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (e.getPlayer().hasPermission("rocketplaceholders.update")) {
            new UpdateChecker(this, 82520).getVersion(version -> {
                if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    p.sendMessage("&e&l&m---------------------------------------".replace("&", "§"));
                    p.sendMessage("&c&lRocket&e&lPlaceholders &f&l» &7There is a new update available.".replace("&", "§"));
                    p.sendMessage("&c&lRocket&e&lPlaceholders &f&l» &7Download it from: &ehttps://bit.ly/RocketPlaceholders".replace("&", "§"));
                    p.sendMessage("&e&l&m---------------------------------------".replace("&", "§"));
                }
            });
        }
    }
}
