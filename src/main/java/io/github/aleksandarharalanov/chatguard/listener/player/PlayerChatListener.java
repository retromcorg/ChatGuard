package io.github.aleksandarharalanov.chatguard.listener.player;

import io.github.aleksandarharalanov.chatguard.core.config.CaptchaConfig;
import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.config.SpamPreventionConfig;
import io.github.aleksandarharalanov.chatguard.core.security.common.TimeFormatter;
import io.github.aleksandarharalanov.chatguard.core.security.captcha.CaptchaDetector;
import io.github.aleksandarharalanov.chatguard.core.security.captcha.CaptchaHandler;
import io.github.aleksandarharalanov.chatguard.core.security.filter.FilterHandler;
import io.github.aleksandarharalanov.chatguard.core.security.penalty.PenaltyEnforcer;
import io.github.aleksandarharalanov.chatguard.core.security.spam.ChatRateLimiter;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerChatListener extends PlayerListener {

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        if (isPlayerMuted(player, event)) return;
        if (hasBypassPermission(player)) return;
        if (handleActiveCaptchaVerification(player, event)) return;
        if (handleSpamPrevention(player, event)) return;
        if (handleChatFiltering(player, event)) return;
        if (handleCaptchaTriggerCheck(player, event)) return;
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

    private static boolean handleActiveCaptchaVerification(Player player, PlayerChatEvent event) {
        if (CaptchaConfig.getEnabled() && CaptchaHandler.doesPlayerHaveActiveCaptcha(player)) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private static boolean handleSpamPrevention(Player player, PlayerChatEvent event) {
        if (SpamPreventionConfig.getChatEnabled() && ChatRateLimiter.isPlayerChatSpamming(player)) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private static boolean handleChatFiltering(Player player, PlayerChatEvent event) {
        if (FilterConfig.getChatEnabled() && FilterHandler.isChatContentBlocked(player, event.getMessage())) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private static boolean handleCaptchaTriggerCheck(Player player, PlayerChatEvent event) {
        if (CaptchaConfig.getEnabled() && CaptchaDetector.doesPlayerTriggerCaptcha(player.getName(), event.getMessage())) {
            CaptchaHandler.processCaptchaTrigger(player, event.getMessage());
            event.setCancelled(true);
            return true;
        }
        return false;
    }
}
