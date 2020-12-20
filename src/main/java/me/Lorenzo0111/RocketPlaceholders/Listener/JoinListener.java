package me.Lorenzo0111.RocketPlaceholders.Listener;

import me.Lorenzo0111.RocketPlaceholders.RocketPlaceholders;
import me.Lorenzo0111.RocketPlaceholders.Updater.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    /*

    Plugin by Lorenzo0111 - https://github.com/Lorenzo0111

     */

    private final RocketPlaceholders plugin;
    private final UpdateChecker checker;

    public JoinListener(RocketPlaceholders plugin) {
        this.plugin = plugin;
        this.checker = new UpdateChecker(this.plugin, 82678);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("rocketplaceholders.update")) {
            if (!plugin.getConfig().getBoolean("update-message")) {
                checker.playerUpdateCheck(event.getPlayer());
            }
        }

    }
}
