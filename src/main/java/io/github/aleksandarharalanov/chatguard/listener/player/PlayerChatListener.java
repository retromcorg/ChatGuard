package io.github.aleksandarharalanov.chatguard.listener.player;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

import java.util.Arrays;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.handler.CaptchaHandler.checkPlayerCaptcha;
import static io.github.aleksandarharalanov.chatguard.handler.CaptchaHandler.isPlayerCaptchaActive;
import static io.github.aleksandarharalanov.chatguard.handler.FilterHandler.checkPlayerMessage;
import static io.github.aleksandarharalanov.chatguard.handler.spam.MessageSpamHandler.isPlayerMessageSpamming;
import static io.github.aleksandarharalanov.chatguard.util.AccessUtil.hasPermission;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logWarning;
import static org.bukkit.Bukkit.getServer;

public class PlayerChatListener extends PlayerListener {

    @Override
    public void onPlayerChat(final PlayerChatEvent event) {
        Player player = event.getPlayer();

        Essentials essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        if (essentials != null && essentials.isEnabled()) {
            User user = essentials.getUser(player.getName());
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

                player.sendMessage(translate(timeMessage.toString()));
                return;
            }
        }

        if (hasPermission(player, "chatguard.bypass")) return;

        boolean isCaptchaEnabled = getConfig().getBoolean("captcha.enabled", true);
        if (isCaptchaEnabled)
            if (isPlayerCaptchaActive(player)) {
                event.setCancelled(true);
                return;
            }

        boolean isMessageSpamPreventionEnabled = getConfig().getBoolean("spam-prevention.enabled.message", true);
        if (isMessageSpamPreventionEnabled)
            if (isPlayerMessageSpamming(player)) {
                event.setCancelled(true);
                return;
            }

        boolean isFilterEnabled = getConfig().getBoolean("filter.enabled", true);
        if (isFilterEnabled) {
            try {
                if (checkPlayerMessage(player, event.getMessage())) {
                    event.setCancelled(true);
                    return;
                }
            } catch (Exception e) {
                logWarning(Arrays.toString(e.getStackTrace()));
            }
        }

        if (isCaptchaEnabled)
            if (checkPlayerCaptcha(player, event.getMessage()))
                event.setCancelled(true);
    }
}
