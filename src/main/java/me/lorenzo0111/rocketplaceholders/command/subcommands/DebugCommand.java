package me.lorenzo0111.rocketplaceholders.command.subcommands;

import me.lorenzo0111.rocketplaceholders.command.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DebugCommand extends SubCommand {
    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        this.getCommand().getDebugger().debug();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getCommand().getPlugin().getConfig().getString("prefix") + "&r &7Debug informations printed in the console."));

    }
}
