package me.lorenzo0111.rocketplaceholders.listener;

import me.lorenzo0111.rocketplaceholders.RocketPlaceholders;
import me.lorenzo0111.rocketplaceholders.updater.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    /*

    Plugin by Lorenzo0111 - https://github.com/Lorenzo0111

     */

    private final RocketPlaceholders plugin;
    private final UpdateChecker updateChecker;

    public JoinListener(RocketPlaceholders plugin) {
        this.plugin = plugin;
        this.updateChecker = new UpdateChecker(this.plugin, 82678);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("rocketplaceholders.update")) {
            if (!this.plugin.getConfig().getBoolean("update-message")) {
                this.updateChecker.sendUpdateCheck(event.getPlayer());
            }
        }

    }
}
