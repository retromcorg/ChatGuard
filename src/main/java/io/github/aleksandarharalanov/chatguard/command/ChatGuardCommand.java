package io.github.aleksandarharalanov.chatguard.command;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.*;
import static io.github.aleksandarharalanov.chatguard.util.AboutUtil.about;
import static io.github.aleksandarharalanov.chatguard.util.AccessUtil.hasPermission;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logInfo;

public class ChatGuardCommand implements CommandExecutor {

    private final ChatGuard plugin;

    public ChatGuardCommand(ChatGuard plugin) {
        this.plugin = plugin;
    }

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
                    about(sender, plugin);
                    break;
                case "reload":
                    reloadCommand(sender);
                    break;
                default:
                    helpCommand(sender);
            }
            return true;
        }

        if (args.length <= 3) {
            if (args[0].equalsIgnoreCase("strikes")) {
                if (!hasPermission(sender, "chatguard.config", "You don't have permission to modify the ChatGuard config.")) return true;

                String playerUsername = args[1];
                List<String> keys = getStrikes().getKeys();
                String foundKey = keys.stream()
                        .filter(key -> key.equalsIgnoreCase(playerUsername))
                        .findFirst()
                        .orElse(null);
                int playerStrikes = foundKey != null ? getStrikes().getInt(foundKey, 0) : -1;

                switch (args.length) {
                    case 2:
                        if (foundKey != null) sender.sendMessage(translate(String.format("&e%s&7 has &c%d &7strike(s).", foundKey, playerStrikes)));
                        else playerNotFound(sender, playerUsername);
                        break;
                    case 3:
                        if (foundKey != null) setPlayerStrikes(sender, foundKey, playerStrikes, args[2]);
                        else playerNotFound(sender, playerUsername);
                        break;
                }
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
                "&e/cg strikes [username] &7- View strikes for player.",
                "&e/cg strikes [username] [0-5] &7- Set strikes for player."
        };

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

    private static void playerNotFound(CommandSender sender, String playerUsername) {
        sender.sendMessage(translate(String.format("&c%s does not exist in the strikes config.", playerUsername)));
    }

    private static void setPlayerStrikes(CommandSender sender, String playerUsername, int oldStrikes, String setStrikes) {
        int newStrikes;
        try {
            newStrikes = Integer.parseInt(setStrikes);
        } catch (NumberFormatException e) {
            sender.sendMessage(translate("&cInvalid input. Please enter a number from 0 to 5."));
            return;
        }

        if (newStrikes < 0 || newStrikes > 5) {
            sender.sendMessage(translate("&cInvalid range. Please choose from 0 to 5."));
            return;
        }

        getStrikes().setProperty(playerUsername, newStrikes);
        getStrikes().saveConfig();

        sender.sendMessage(translate(String.format(
                "&e%s&7 strikes set from &c%d &7to &c%d&7.", playerUsername, oldStrikes, newStrikes
        )));
    }
}
