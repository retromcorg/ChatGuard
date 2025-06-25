package io.github.aleksandarharalanov.chatguard.listener.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.security.common.TimeFormatter;
import io.github.aleksandarharalanov.chatguard.core.security.filter.FilterHandler;
import io.github.aleksandarharalanov.chatguard.core.security.penalty.PenaltyEnforcer;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;

public class PlayerChatListener extends PlayerListener {

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        if (isPlayerMuted(player, event)) return;
        if (hasBypassPermission(player)) return;
        if (handleChatFiltering(player, event)) return;
    }

    private static boolean isPlayerMuted(Player player, PlayerChatEvent event) {
        if (PenaltyEnforcer.getMuteHandler() == null) return false;

        if (PenaltyEnforcer.getMuteHandler().isPlayerMuted(player.getName())) {
            TimeFormatter.printFormattedMuteDuration(player.getName());
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private static boolean hasBypassPermission(Player player) {
        return AccessUtil.senderHasPermission(player, "chatguard.bypass");
    }

    private static boolean handleChatFiltering(Player player, PlayerChatEvent event) {
        if (FilterConfig.getChatEnabled() && FilterHandler.isChatContentBlocked(player, event.getMessage())) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }
}
