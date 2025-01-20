package io.github.aleksandarharalanov.chatguard.handler;

import org.bukkit.event.player.PlayerChatEvent;

import java.util.HashMap;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.ChatGuard.getStrikes;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;

public class SpamHandler {

    private static final HashMap<String, Long> playerMessageTimestamps = new HashMap<>();

    public static boolean isPlayerSpamming(PlayerChatEvent event) {
        long timestamp = System.currentTimeMillis();
        Long lastTimestamp = playerMessageTimestamps.get(event.getPlayer().getName());
        if (lastTimestamp != null) {
            long elapsed = timestamp - lastTimestamp;
            int strikeTier = getStrikes().getInt(event.getPlayer().getName(), 0);
            int cooldown = getConfig().getInt(String.format("spam-prevention.cooldown-ms.s%d", strikeTier), 0);

            if (elapsed <= cooldown) {
                boolean isWarnEnabled = getConfig().getBoolean("spam-prevention.warn-player", true);
                if (isWarnEnabled) {
                    double remainingTime = (cooldown - elapsed) / 1000.0;
                    event.getPlayer().sendMessage(translate(String.format("&cPlease wait %.2f sec. before sending another message.", remainingTime)));
                }

                event.setCancelled(true);
                return true;
            }
        }

        playerMessageTimestamps.put(event.getPlayer().getName(), timestamp);
        return false;
    }
}
