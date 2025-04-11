package io.github.aleksandarharalanov.chatguard.command.subcommand;

import io.github.aleksandarharalanov.chatguard.core.config.PenaltyConfig;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class StrikeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!AccessUtil.senderHasPermission(sender, "chatguard.config", "[ChatGuard] You don't have permission to modify the config.")) {
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ColorUtil.translateColorCodes("&cUsage: /cg strike <username> [0-5]"));
            return true;
        }

        String playerName = args[1];
        List<String> keys = PenaltyConfig.getStrikesKeys();
        String foundKey = keys.stream()
                .filter(key -> key.equalsIgnoreCase(playerName))
                .findFirst()
                .orElse(null);
        int playerStrikeTier = foundKey != null ? PenaltyConfig.getPlayerStrike(foundKey) : -1;

        if (foundKey == null) {
            sender.sendMessage(ColorUtil.translateColorCodes(String.format(
                    "&c[ChatGuard] Player &e%s &cnot found.",
                    playerName
            )));
            return true;
        }

        if (args.length == 2) {
            sender.sendMessage(ColorUtil.translateColorCodes(String.format(
                    "&a[ChatGuard] &e%s &ais on strike &e%d&a.",
                    foundKey, playerStrikeTier
            )));
            return true;
        }

        try {
            int newStrike = Integer.parseInt(args[2]);
            if (newStrike < 0) {
                sender.sendMessage(ColorUtil.translateColorCodes("&c[ChatGuard] Must be greater than &e0."));
                return true;
            }

            PenaltyConfig.setPlayerStrike(playerName, newStrike);

            if (sender instanceof Player) {
                sender.sendMessage(ColorUtil.translateColorCodes(String.format(
                        "&a[ChatGuard] &e%s &astrike set from &e%d &ato &e%d&a.",
                        foundKey, playerStrikeTier, newStrike
                )));
            }

            System.out.println(String.format(
                    "[ChatGuard] Player '%s' set from strike %d to %d.",
                    foundKey, playerStrikeTier, newStrike
            ));
        } catch (NumberFormatException e) {
            sender.sendMessage(ColorUtil.translateColorCodes("&c[ChatGuard] Invalid input. Enter a number."));
        }

        return true;
    }
}
