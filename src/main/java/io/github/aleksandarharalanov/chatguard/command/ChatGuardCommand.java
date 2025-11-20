package io.github.aleksandarharalanov.chatguard.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.command.subcommand.AboutCommand;
import io.github.aleksandarharalanov.chatguard.command.subcommand.FilterCommand;
import io.github.aleksandarharalanov.chatguard.command.subcommand.HelpCommand;
import io.github.aleksandarharalanov.chatguard.command.subcommand.IChatGuardSubCommand;
import io.github.aleksandarharalanov.chatguard.command.subcommand.ReloadCommand;
import io.github.aleksandarharalanov.chatguard.command.subcommand.StrikeCommand;

public final class ChatGuardCommand implements CommandExecutor {

    private final Map<String, IChatGuardSubCommand> subcommands = new HashMap<>();

    public ChatGuardCommand(ChatGuard plugin) {
        subcommands.put("about", new AboutCommand(plugin));
        subcommands.put("reload", new ReloadCommand());
        subcommands.put("strike", new StrikeCommand());
        subcommands.put("filter", new FilterCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            return sendHelp(sender);

        IChatGuardSubCommand subcommand = subcommands.get(args[0].toLowerCase());

        if (subcommand == null)
            return sendHelp(sender);

        if(!subcommand.onCommand(sender, args))
            return sendHelp(sender);

        return true;
    }

    public boolean sendHelp(CommandSender sender) {
        HelpCommand.sendHelp(sender);
        return true;
    }
}
