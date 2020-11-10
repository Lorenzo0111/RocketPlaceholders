package me.Lorenzo0111.RocketPlaceholders.Listener;

import me.Lorenzo0111.RocketPlaceholders.Placeholders;
import me.Lorenzo0111.RocketPlaceholders.Updater.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    /*

    Plugin by Lorenzo0111 - https://github.com/Lorenzo0111

     */

    private final Placeholders plugin;

    public Join(Placeholders plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (event.getPlayer().hasPermission("rocketplaceholders.update")) {
            if (!plugin.getConfig().getBoolean("update-message")) {
                return;
            }
            new UpdateChecker(plugin, plugin.code).getVersion(version -> {
                if (!plugin.getDescription().getVersion().equalsIgnoreCase(version)) {
                    player.sendMessage("&e&l&m---------------------------------------".replace("&", "§"));
                    player.sendMessage("&c&lRocket&e&lPlaceholders &f&l» &7There is a new update available.".replace("&", "§"));
                    player.sendMessage("&c&lRocket&e&lPlaceholders &f&l» &7Download it from: &ehttps://bit.ly/RocketPlaceholders".replace("&", "§"));
                    player.sendMessage("&e&l&m---------------------------------------".replace("&", "§"));
                }
            });
        }
    }
}
