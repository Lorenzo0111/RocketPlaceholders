package me.lorenzo0111.rocketplaceholders.command.subcommands;

import me.lorenzo0111.rocketplaceholders.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        this.getCommand().getPlugin().reloadConfig();
        this.getCommand().getChecker().sendUpdateCheck(sender);

        this.getCommand().getPlugin().getLoader().loadDatabaseManager();

        if (this.getCommand().getPlugin().getLoader().getDatabaseManager() != null && !this.getCommand().getPlugin().getConfig().getBoolean("mysql.enabled")) {
            this.getCommand().getPlugin().getLoader().setDatabaseManager(null);
        }

        this.getCommand().getPlaceholdersManager().reload();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7Plugin reloaded!"));
    }
}
