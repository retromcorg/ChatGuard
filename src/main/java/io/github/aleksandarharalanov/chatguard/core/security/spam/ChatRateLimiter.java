package io.github.aleksandarharalanov.chatguard.core.security.spam;

import io.github.aleksandarharalanov.chatguard.core.config.PenaltyConfig;
import io.github.aleksandarharalanov.chatguard.core.config.SpamPreventionConfig;
import io.github.aleksandarharalanov.chatguard.core.data.TimestampData;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;

public final class ChatRateLimiter {

    private ChatRateLimiter() {}

    public static boolean isPlayerChatSpamming(Player player) {
        long timestamp = System.currentTimeMillis();
        String playerName = player.getName();

        long lastTimestamp = TimestampData.getMessageTimestamp(playerName);

        if (lastTimestamp != 0) {
            long elapsed = timestamp - lastTimestamp;
            int cooldown = SpamPreventionConfig.getCooldownMsChat().get(PenaltyConfig.getPlayerStrike(player));

            if (elapsed <= cooldown) {
                if (SpamPreventionConfig.getWarnPlayerEnabled()) {
                    double remainingTime = (cooldown - elapsed) / 1000.0;
                    player.sendMessage(ColorUtil.translateColorCodes(String.format(
                            "&cPlease wait %.2f sec. before sending another message.", remainingTime
                    )));
                }
                return true;
            }
        }

        TimestampData.setMessageTimestamp(playerName, timestamp);
        return false;
    }
}

