package io.github.aleksandarharalanov.chatguard.handler.spam;

import org.bukkit.entity.Player;

import java.util.HashMap;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.ChatGuard.getStrikes;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;

public class CommandSpamHandler {

    private static final HashMap<String, Long> playerCommandTimestamps = new HashMap<>();

    public static boolean isPlayerCommandSpamming(Player player) {
        long timestamp = System.currentTimeMillis();

        Long lastTimestamp = playerCommandTimestamps.get(player.getName());
        if (lastTimestamp != null) {
            long elapsed = timestamp - lastTimestamp;

            int strikeTier = getStrikes().getInt(player.getName(), 0);
            int cooldown = getConfig().getInt(String.format("spam-prevention.cooldown-ms.command.s%d", strikeTier), 0);

            if (elapsed <= cooldown) {
                boolean isWarnEnabled = getConfig().getBoolean("spam-prevention.warn-player", true);
                if (isWarnEnabled) {
                    double remainingTime = (cooldown - elapsed) / 1000.0;
                    player.sendMessage(translate(String.format("&cPlease wait %.2f sec. before running another command.", remainingTime)));
                }

                return true;
            }
        }

        playerCommandTimestamps.put(player.getName(), timestamp);
        return false;
    }
}
