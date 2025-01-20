package io.github.aleksandarharalanov.chatguard.listener;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

import java.util.Arrays;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.handler.CaptchaHandler.checkPlayerCaptcha;
import static io.github.aleksandarharalanov.chatguard.handler.CaptchaHandler.isPlayerCaptchaActive;
import static io.github.aleksandarharalanov.chatguard.handler.FilterHandler.checkPlayerMessage;
import static io.github.aleksandarharalanov.chatguard.handler.SpamHandler.isPlayerSpamming;
import static io.github.aleksandarharalanov.chatguard.util.AccessUtil.hasPermission;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logWarning;
import static org.bukkit.Bukkit.getServer;

public class PlayerChatListener extends PlayerListener {

    @Override
    public void onPlayerChat(final PlayerChatEvent event) {
        Essentials essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        if (essentials != null && essentials.isEnabled()) {
            User user = essentials.getUser(event.getPlayer().getName());
            if (user.isMuted()) {
                event.setCancelled(true);

                long remainingMillis = user.getMuteTimeout() - System.currentTimeMillis();
                int seconds = (int) ((remainingMillis / 1000) % 60);
                int minutes = (int) ((remainingMillis / (1000 * 60)) % 60);
                int hours = (int) ((remainingMillis / (1000 * 60 * 60)) % 24);
                int days = (int) (remainingMillis / (1000 * 60 * 60 * 24));

                StringBuilder timeMessage = new StringBuilder("&7");
                if (days > 0) timeMessage.append(days).append(" day(s)");
                if (hours > 0) {
                    if (timeMessage.length() > 2) timeMessage.append(", ");
                    timeMessage.append(hours).append(" hour(s)");
                }
                if (minutes > 0) {
                    if (timeMessage.length() > 2) timeMessage.append(", ");
                    timeMessage.append(minutes).append(" minute(s)");
                }
                if (seconds > 0) {
                    if (timeMessage.length() > 2) timeMessage.append(", ");
                    timeMessage.append(seconds).append(" second(s)");
                }

                event.getPlayer().sendMessage(translate(timeMessage.toString()));
                return;
            }
        }

        if (hasPermission(event.getPlayer(), "chatguard.bypass")) return;

        boolean isCaptchaEnabled = getConfig().getBoolean("captcha.enabled", true);
        if (isCaptchaEnabled)
            if (isPlayerCaptchaActive(event)) return;

        boolean isSpamPreventionEnabled = getConfig().getBoolean("spam-prevention.enabled", true);
        if (isSpamPreventionEnabled)
            if (isPlayerSpamming(event)) return;

        boolean isFilterEnabled = getConfig().getBoolean("filter.enabled", true);
        if (isFilterEnabled) {
            try {
                checkPlayerMessage(event);
            } catch (Exception e) {
                logWarning(Arrays.toString(e.getStackTrace()));
            }
        }

        if (isCaptchaEnabled) checkPlayerCaptcha(event);
    }
}
