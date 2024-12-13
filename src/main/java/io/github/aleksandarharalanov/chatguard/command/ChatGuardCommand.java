package io.github.aleksandarharalanov.chatguard.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.*;
import static io.github.aleksandarharalanov.chatguard.util.AboutUtil.about;
import static io.github.aleksandarharalanov.chatguard.util.AccessUtil.hasPermission;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logInfo;

public class ChatGuardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("chatguard") &&
                !command.getName().equalsIgnoreCase("cg"))
            return true;

        if (args.length == 0) {
            helpCommand(sender);
            return true;
        }

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "about":
                    about(sender, getInstance());
                    break;
                case "reload":
                    reloadCommand(sender);
                    break;
                default:
                    helpCommand(sender);
            }
            return true;
        }

        helpCommand(sender);
        return true;
    }

    private static void helpCommand(CommandSender sender) {
        String[] messages = {
                "&bChatGuard commands:",
                "&e/cg &7- Displays this message.",
                "&e/cg about &7- About ChatGuard.",
                "&bChatGuard staff commands:",
                "&e/cg reload &7- Reload ChatGuard config.",
        };

        sendHelpCommands(sender, messages);
    }

    private static void sendHelpCommands(CommandSender sender, String[] messages) {
        for (String message : messages) {
            if (sender instanceof Player) sender.sendMessage(translate(message));
            else logInfo(message.replaceAll("&.", ""));
        }
    }
    
    private static void reloadCommand(CommandSender sender) {
        if (!hasPermission(sender, "chatguard.config", "You don't have permission to reload the ChatGuard config.")) return;

        getConfig().loadConfig();
        getStrikes().loadConfig();

        if (sender instanceof Player) sender.sendMessage(translate("&aChatGuard config reloaded."));
    }
}
