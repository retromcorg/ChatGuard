package io.github.aleksandarharalanov.chatguard.command;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.*;
import static io.github.aleksandarharalanov.chatguard.handler.CaptchaHandler.getPlayerCaptcha;
import static io.github.aleksandarharalanov.chatguard.handler.PunishmentHandler.getStrike;
import static io.github.aleksandarharalanov.chatguard.handler.SoundHandler.playSoundCue;
import static io.github.aleksandarharalanov.chatguard.util.AboutUtil.about;
import static io.github.aleksandarharalanov.chatguard.util.AccessUtil.commandInGameOnly;
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
        if (!command.getName().equalsIgnoreCase("chatguard") && !command.getName().equalsIgnoreCase("cg"))
            return true;

        if (args.length == 0) {
            helpCommand(sender);
            return true;
        }

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "about":
                    // Contributors: Add your name here, if you wish to be credited, when you contribute code
                    List<String> contributorsList = new ArrayList<>(Arrays.asList(
                            "moderator_man"
                    ));
                    about(sender, plugin, contributorsList);
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
            switch (args[0].toLowerCase()) {
                case "strike":
                    if (!hasPermission(sender, "chatguard.config", "[ChatGuard] You don't have permission to modify the config.")) break;

                    String playerName = args[1];
                    List<String> keys = getStrikes().getKeys();
                    String foundKey = keys.stream()
                            .filter(key -> key.equalsIgnoreCase(playerName))
                            .findFirst()
                            .orElse(null);
                    int playerStrike = foundKey != null ? getStrike(foundKey) : -1;

                    switch (args.length) {
                        case 2:
                            if (foundKey != null) sender.sendMessage(translate(String.format("&c[ChatGuard] &e%s &cis on strike &e%d&c.", foundKey, playerStrike)));
                            else playerNotFound(sender, playerName);
                            break;
                        case 3:
                            if (foundKey != null) setPlayerStrikes(sender, foundKey, playerStrike, args[2]);
                            else playerNotFound(sender, playerName);
                            break;
                    }
                    break;
                case "captcha":
                    if (commandInGameOnly(sender)) break;

                    Player player = (Player) sender;
                    String captchaCode = getPlayerCaptcha().get(player.getName());
                    if (captchaCode != null) {
                        String input = args[1];
                        if (input.equals(captchaCode)) {
                            getPlayerCaptcha().remove(player.getName());
                            player.sendMessage(translate("&a[ChatGuard] Captcha verification passed."));
                            playSoundCue(player,true);
                            logInfo(String.format("[ChatGuard] Player '%s' passed captcha verification.", player.getName()));
                        } else {
                            player.sendMessage(translate("&c[ChatGuard] Please enter the correct captcha code."));
                            playSoundCue(player,false);
                        }
                    } else player.sendMessage(translate("&c[ChatGuard] You don't have an active captcha verification."));
                    break;
                default:
                    break;
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
                "&e/cg captcha <code> &7- Captcha verification.",
                "&bChatGuard staff commands:",
                "&e/cg reload &7- Reload ChatGuard config.",
                "&e/cg strike <username> &7- View strike of player.",
                "&e/cg strike <username> [0-5] &7- Set strike of player."
        };

        for (String message : messages) {
            if (sender instanceof Player) sender.sendMessage(translate(message));
            else logInfo(message.replaceAll("&.", ""));
        }
    }

    private static void playerNotFound(CommandSender sender, String playerName) {
        sender.sendMessage(translate(String.format("&c[ChatGuard] &e%s &cdoes not exist in the configuration.", playerName)));
    }

    private static void reloadCommand(CommandSender sender) {
        if (!hasPermission(sender, "chatguard.config", "[ChatGuard] You don't have permission to reload the config.")) return;

        if (sender instanceof Player) sender.sendMessage(translate("&a[ChatGuard] Configurations reloaded."));
        logInfo("[ChatGuard] Configurations reloaded.");
        getConfig().loadConfig();
        getStrikes().loadConfig();
    }

    private static void setPlayerStrikes(CommandSender sender, String playerName, int oldStrike, String setStrike) {
        int newStrike;

        try {
            newStrike = Integer.parseInt(setStrike);
        } catch (NumberFormatException e) {
            sender.sendMessage(translate("&c[ChatGuard] Invalid input. Please enter a number from 0 to 5."));
            return;
        }

        if (newStrike < 0 || newStrike > 5) {
            sender.sendMessage(translate("&c[ChatGuard] Invalid range. Please choose from &e0&c to &e5."));
            return;
        }

        getStrikes().setProperty(playerName, newStrike);
        getStrikes().save();

        if (sender instanceof Player)
            sender.sendMessage(translate(String.format("&c[ChatGuard] &e%s &cset from strike &e%d &cto &e%d&c.", playerName, oldStrike, newStrike)));

        logInfo(String.format("[ChatGuard] Player '%s' set from strike %d to %d.", playerName, oldStrike, newStrike));
    }
}
