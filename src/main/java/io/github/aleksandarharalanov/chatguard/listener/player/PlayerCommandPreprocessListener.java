package io.github.aleksandarharalanov.chatguard.listener.player;

import io.github.aleksandarharalanov.chatguard.core.config.SpamPreventionConfig;
import io.github.aleksandarharalanov.chatguard.core.security.spam.CommandRateLimiter;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerCommandPreprocessListener extends PlayerListener {

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (hasBypassPermission(player)) return;
        if (handleSpamPrevention(player, event)) return;
    }

    private static boolean hasBypassPermission(Player player) {
        return AccessUtil.senderHasPermission(player, "chatguard.bypass");
    }

    private static boolean handleSpamPrevention(Player player, PlayerCommandPreprocessEvent event) {
        if (SpamPreventionConfig.getCommandEnabled() && CommandRateLimiter.isPlayerCommandSpamming(player, event.getMessage())) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }
}
