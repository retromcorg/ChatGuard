package io.github.aleksandarharalanov.chatguard.listener;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

import java.util.Arrays;
import java.util.HashMap;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.ChatGuard.getStrikes;
import static io.github.aleksandarharalanov.chatguard.handler.MessageHandler.checkMessage;
import static io.github.aleksandarharalanov.chatguard.util.AccessUtil.hasPermission;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logWarning;
import static org.bukkit.Bukkit.getServer;

public class PlayerChatListener extends PlayerListener {
    private static final HashMap<String, Long> playerTimestamps = new HashMap<>();

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        Essentials essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        if (essentials != null) {
            User user = essentials.getUser(player.getName());
            if (user.isMuted()) {
                event.setCancelled(true);
                return;
            }
        }

        if (hasPermission(player, "chatguard.bypass")) return;

        boolean isSpamPreventionEnabled = getConfig().getBoolean("spam-prevention.enabled", true);
        if (isSpamPreventionEnabled) {
            long timestamp = System.currentTimeMillis();
            Long lastTimestamp = playerTimestamps.get(player.getName());
            if (lastTimestamp != null) {
                long elapsed = timestamp - lastTimestamp;
                int strikeTier = getStrikes().getInt(player.getName(), 0);
                int cooldown = getConfig().getInt(String.format("spam-prevention.cooldown-ms.s%d", strikeTier), 0);

                if (elapsed <= cooldown) {
                    boolean isWarnEnabled = getConfig().getBoolean("spam-prevention.warn-player", true);
                    if (isWarnEnabled) {
                        double remainingTime = (cooldown - elapsed) / 1000.0;
                        player.sendMessage(translate(String.format("&cPlease wait %.2f sec. before sending another message.", remainingTime)));
                    }
                    event.setCancelled(true);
                    return;
                }
            }
            playerTimestamps.put(player.getName(), timestamp);
        }

        boolean isFilterEnabled = getConfig().getBoolean("filter.enabled", true);
        if (isFilterEnabled) {
            try {
                checkMessage(event);
            } catch (Exception e) {
                logWarning(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
