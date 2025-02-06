package io.github.aleksandarharalanov.chatguard.handler;

import io.github.aleksandarharalanov.chatguard.util.DiscordUtil;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.ChatGuard.getInstance;
import static io.github.aleksandarharalanov.chatguard.handler.FilterHandler.*;
import static io.github.aleksandarharalanov.chatguard.handler.PunishmentHandler.getMuteDuration;
import static io.github.aleksandarharalanov.chatguard.handler.PunishmentHandler.getStrike;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logWarning;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.writeToLog;
import static org.bukkit.Bukkit.getServer;

public class LogHandler {

    public static void performLogs(String detection, Player player, String message) {
        boolean isConsoleLogEnabled = getConfig().getBoolean(String.format("%s.log.console", detection), true);
        if (isConsoleLogEnabled) {
            if (detection.equals("filter")) logWarning(String.format("[ChatGuard] <%s> %s", player.getName(), message));
            else logWarning(String.format("[ChatGuard] Detected player '%s' for bot-like behavior. Prompted captcha verification.", player.getName()));
        }

        boolean isLocalFileLogEnabled = getConfig().getBoolean(String.format("%s.log.local-file", detection), true);
        if (isLocalFileLogEnabled) {
            if (detection.equals("filter")) writeToLog(String.format("[FILTER] [%s] <%s> %s", player.getAddress().getAddress().getHostAddress(), player.getName(), message), true);
            else writeToLog(String.format("[CAPTCHA] [%s] <%s> %s", player.getAddress().getAddress().getHostAddress(), player.getName(), message), true);
        }

        boolean isDiscordWebhookLogEnabled = getConfig().getBoolean(String.format("%s.log.discord-webhook.enabled", detection), true);
        if (isDiscordWebhookLogEnabled) {
            getServer().getScheduler().scheduleAsyncDelayedTask(getInstance(), () -> {
                DiscordUtil webhook = new DiscordUtil(getConfig().getString(String.format("%s.log.discord-webhook.url", detection)));
                webhook.setUsername("ChatGuard");
                webhook.setAvatarUrl("https://raw.githubusercontent.com/AleksandarHaralanov/ChatGuard/refs/heads/master/assets/ChatGuard-Logo.png");

                final long unixTimestamp = System.currentTimeMillis() / 1000;
                DiscordUtil.EmbedObject embed = new DiscordUtil.EmbedObject();

                if (detection.equals("filter")) embed.setTitle("Filter Detection");
                else embed.setTitle("Captcha Verification");

                embed.setAuthor(player.getName(), null, String.format("https://minotar.net/helm/%s.png", player.getName()));

                if (detection.equals("filter")) {
                    if (getStrike(player) <= 4) embed.setDescription(String.format("S%d > S%d ・ Mute Duration: %s", getStrike(player), getStrike(player) + 1, getMuteDuration(player)));
                    else embed.setDescription(String.format("S%d ・ Mute Duration: %s", getStrike(player), getMuteDuration(player)));
                }

                embed.addField("Message:", message, false);

                if (detection.equals("filter")) embed.addField("Trigger:", String.format("`%s`", getTrigger()), true);

                embed.addField("IP:", player.getAddress().getAddress().getHostAddress(), true)
                        .addField("Timestamp:", String.format("<t:%d:f>", unixTimestamp), true)
                        .setFooter(String.format("ChatGuard v%s ・ Logger", getInstance().getDescription().getVersion()), null);

                if (detection.equals("filter")) embed.setColor(new Color(178, 34, 34));
                else embed.setColor(new Color(0, 152, 186));

                webhook.addEmbed(embed);

                try {
                    webhook.execute();
                } catch (IOException e) {
                    logWarning(Arrays.toString(e.getStackTrace()));
                }
            }, 0L);
        }
    }
}
