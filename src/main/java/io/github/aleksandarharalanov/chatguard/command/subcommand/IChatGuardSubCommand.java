package io.github.aleksandarharalanov.chatguard.command.subcommand;

import org.bukkit.command.CommandSender;

public interface IChatGuardSubCommand {

    public boolean onCommand(CommandSender sender, String[] args);

}
