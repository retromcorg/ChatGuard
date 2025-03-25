package io.github.aleksandarharalanov.chatguard.core.security.spam;

import io.github.aleksandarharalanov.chatguard.core.config.PenaltyConfig;
import io.github.aleksandarharalanov.chatguard.core.config.SpamPreventionConfig;
import io.github.aleksandarharalanov.chatguard.core.data.TimestampData;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;

public final class CommandRateLimiter {

    private CommandRateLimiter() {}

    public static boolean isPlayerCommandSpamming(Player player, String message) {
        String command = message.split(" ")[0].substring(1);
        if (SpamPreventionConfig.getCommandWhitelist().contains(command)) {
            return false;
        }

        long timestamp = System.currentTimeMillis();
        String playerName = player.getName();

        long lastTimestamp = TimestampData.getCommandTimestamp(playerName);

        if (lastTimestamp != 0) {
            long elapsed = timestamp - lastTimestamp;
            int cooldown = SpamPreventionConfig.getCooldownMsCommand().get(PenaltyConfig.getPlayerStrike(player));

            if (elapsed <= cooldown) {
                if (SpamPreventionConfig.getWarnPlayerEnabled()) {
                    double remainingTime = (cooldown - elapsed) / 1000.0;
                    player.sendMessage(ColorUtil.translateColorCodes(String.format(
                            "&cPlease wait %.2f sec. before running another command.", remainingTime
                    )));
                }
                return true;
            }
        }

        TimestampData.setCommandTimestamp(playerName, timestamp);
        return false;
    }
}
