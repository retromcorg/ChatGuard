package io.github.aleksandarharalanov.chatguard.handler;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import io.github.aleksandarharalanov.chatguard.util.DiscordUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.*;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.*;
import static org.bukkit.Bukkit.getServer;

public class MessageHandler {

    private static String trigger;

    public static void checkMessage(PlayerChatEvent event) throws Exception {
        String originalMessage = event.getMessage();
        String safeMessage = event.getMessage().toLowerCase();
        Set<String> terms = new HashSet<>(getConfig().getStringList("filter.rules.terms.whitelist", new ArrayList<>()));
        for (String term : terms) safeMessage = safeMessage.replaceAll(term, "");
        if (containsBannedTerms(safeMessage) || matchesRegexPatterns(safeMessage)) blockMessage(event, originalMessage);
    }

    private static boolean containsBannedTerms(String message) {
        Set<String> terms = new HashSet<>(getConfig().getStringList("filter.rules.terms.blacklist", new ArrayList<>()));
        String[] messageTerms = message.split("\\s+");
        for (String term : messageTerms)
            if (terms.contains(term)) {
                trigger = term;
                return true;
            }
        return false;
    }

    private static boolean matchesRegexPatterns(String message) {
        Set<String> regexList = new HashSet<>(getConfig().getStringList("filter.rules.regex", new ArrayList<>()));
        for (String regex : regexList) {
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                trigger = regex.replace("\\", "\\\\").replace("\"", "\\\"");
                return true;
            }
        }
        return false;
    }

    private static void blockMessage(PlayerChatEvent event, String message) throws Exception {
        event.setCancelled(true);

        Player player = event.getPlayer();
        boolean isWarnEnabled = getConfig().getBoolean("filter.warn-player", true);
        if (isWarnEnabled) player.sendMessage(translate("&cMessage cancelled for containing blocked words."));

        int strike = getStrikes().getInt(player.getName(), 0);
        String muteDuration = getConfig().getString(String.format("filter.mute.duration.s%d", strike));
        performLogs(player, message, strike, muteDuration);
        issuePunishments(player, strike, muteDuration);
    }

    private static void performLogs(Player player, String message, int strike, String muteDuration) {
        boolean isConsoleLogEnabled = getConfig().getBoolean("filter.log.console", true);
        if (isConsoleLogEnabled) logWarning(String.format("[ChatGuard] <%s> %s", player.getName(), message));

        boolean isLocalFileLogEnabled = getConfig().getBoolean("filter.log.local-file", true);
        if (isLocalFileLogEnabled) {
            String hostAddress = player.getAddress().getAddress().getHostAddress();
            String playerName = player.getName();
            writeToLog(String.format("[%s] <%s> %s", hostAddress, playerName, message), true);
        }

        boolean isDiscordWebhookLogEnabled = getConfig().getBoolean("filter.log.discord-webhook.enabled", true);
        if (isDiscordWebhookLogEnabled) {
            getServer().getScheduler().scheduleAsyncDelayedTask(getInstance(), () -> {
                DiscordUtil webhook = new DiscordUtil(getConfig().getString("filter.log.discord-webhook.url"));
                webhook.setUsername("ChatGuard");
                webhook.setAvatarUrl("https://raw.githubusercontent.com/AleksandarHaralanov/ChatGuard/refs/heads/master/assets/ChatGuard-Logo.png");

                final long unixTimestamp = System.currentTimeMillis() / 1000;
                DiscordUtil.EmbedObject embed = new DiscordUtil.EmbedObject();
                embed.setAuthor(player.getName(), null, String.format("https://minotar.net/helm/%s.png", player.getName()))
                        .addField("Message:", message, false)
                        .addField("Trigger:", String.format("`%s`", trigger), true)
                        .addField("IP:", player.getAddress().getAddress().getHostAddress(), true)
                        .addField("Timestamp:", String.format("<t:%d:f>", unixTimestamp), true)
                        .setFooter(String.format("ChatGuard v%s ・ Logger", getInstance().getDescription().getVersion()), null)
                        .setColor(new Color(255, 85, 85));

                if (strike <= 4) embed.setDescription(String.format("S%d > S%d ・ Mute Duration: %s", strike, strike + 1, muteDuration));
                else embed.setDescription(String.format("S%d ・ Mute Duration: %s", strike, muteDuration));

                webhook.addEmbed(embed);

                try {
                    webhook.execute();
                } catch (IOException e) {
                    logWarning(Arrays.toString(e.getStackTrace()));
                }
            }, 0L);
        }
    }

    private static void issuePunishments(Player player, int strike, String muteDuration) throws Exception {
        Essentials essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        boolean isMuteEnabled = getConfig().getBoolean("filter.mute.enabled", true);
        if (essentials != null && essentials.isEnabled() && isMuteEnabled) {
            User user = essentials.getUser(player.getName());
            user.setMuteTimeout(Util.parseDateDiff(muteDuration, true));
            user.setMuted(true);
            getServer().broadcastMessage(translate(String.format(
                    "&c[ChatGuard] %s muted for %s. by system; message contains blocked words.",
                    player.getName(), muteDuration
            )));
        }

        if (strike <= 4) {
            getStrikes().setProperty(player.getName(), strike + 1);
            getStrikes().saveConfig();
        }
    }
}
