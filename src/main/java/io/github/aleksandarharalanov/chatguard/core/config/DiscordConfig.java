package io.github.aleksandarharalanov.chatguard.core.config;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;

public final class DiscordConfig {

    private DiscordConfig() {}

    public static String getWebhookUrl() {
        return ChatGuard.getDiscord().getString("webhook-url");
    }

    public static boolean getDiscordLogEnabled(LogType logType) {
        return ChatGuard.getDiscord().getBoolean(String.format("embed-log.type.%s", logType.getName()), false);
    }

    public static boolean getLogCensorEnabled() {
        return ChatGuard.getDiscord().getBoolean("embed-log.optional.censor", true);
    }

    public static boolean getLogIpEnabled() {
        return ChatGuard.getDiscord().getBoolean("embed-log.optional.data.ip-address", true);
    }

    public static boolean getLogTimestampEnabled() {
        return ChatGuard.getDiscord().getBoolean("embed-log.optional.data.timestamp", true);
    }

    public static String getPlayerAvatar() {
        return ChatGuard.getDiscord().getString("customize.player-avatar", "https://minotar.net/avatar/%player%.png");
    }

    public static String getEmbedColor(LogType logType) {
        return ChatGuard.getDiscord().getString(String.format("customize.type.%s.color", logType.getName()), "#FFFFFF");
    }

    public static String getWebhookName(LogType logType) {
        return ChatGuard.getDiscord().getString(String.format("customize.type.%s.webhook.name", logType.getName()), "ChatGuard");
    }

    public static String getWebhookIcon(LogType logType) {
        return ChatGuard.getDiscord().getString(String.format("customize.type.%s.webhook.icon", logType.getName()),
                "https://raw.githubusercontent.com/AleksandarHaralanov/ChatGuard/refs/heads/master/assets/ChatGuard-Logo.png");
    }
}
