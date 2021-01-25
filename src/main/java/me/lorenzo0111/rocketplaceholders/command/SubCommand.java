package me.lorenzo0111.rocketplaceholders.command;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    protected RocketPlaceholdersCommand command;

    public SubCommand create(RocketPlaceholdersCommand command) {
        this.command = command;
        return this;
    }

    public abstract String getName();

    public RocketPlaceholdersCommand getCommand() {
        return command;
    }

    public abstract void perform(CommandSender sender, String[] args);

}
