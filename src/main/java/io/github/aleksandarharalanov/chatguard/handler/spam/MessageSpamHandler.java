package io.github.aleksandarharalanov.chatguard.handler.spam;

import org.bukkit.entity.Player;

import java.util.HashMap;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.ChatGuard.getStrikes;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;

public class MessageSpamHandler {

    private static final HashMap<String, Long> playerMessageTimestamps = new HashMap<>();

    public static boolean isPlayerMessageSpamming(Player player) {
        long timestamp = System.currentTimeMillis();

        Long lastTimestamp = playerMessageTimestamps.get(player.getName());
        if (lastTimestamp != null) {
            long elapsed = timestamp - lastTimestamp;

            int strikeTier = getStrikes().getInt(player.getName(), 0);
            int cooldown = getConfig().getInt(String.format("spam-prevention.cooldown-ms.message.s%d", strikeTier), 0);

            if (elapsed <= cooldown) {
                boolean isWarnEnabled = getConfig().getBoolean("spam-prevention.warn-player", true);
                if (isWarnEnabled) {
                    double remainingTime = (cooldown - elapsed) / 1000.0;
                    player.sendMessage(translate(String.format("&cPlease wait %.2f sec. before sending another message.", remainingTime)));
                }

                return true;
            }
        }

        playerMessageTimestamps.put(player.getName(), timestamp);
        return false;
    }
}
