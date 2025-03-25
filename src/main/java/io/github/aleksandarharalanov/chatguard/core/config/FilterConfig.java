package io.github.aleksandarharalanov.chatguard.core.config;

import io.github.aleksandarharalanov.chatguard.ChatGuard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FilterConfig {

    private FilterConfig() {}

    public static boolean getChatEnabled() {
        return ChatGuard.getConfig().getBoolean("filter.enabled.chat", true);
    }

    public static boolean getSignEnabled() {
        return ChatGuard.getConfig().getBoolean("filter.enabled.sign", true);
    }

    public static boolean getNameEnabled() {
        return ChatGuard.getConfig().getBoolean("filter.enabled.name", true);
    }

    public static boolean getWarnPlayerEnabled() {
        return ChatGuard.getConfig().getBoolean("filter.warn-player", true);
    }

    public static boolean getLogConsoleEnabled() {
        return ChatGuard.getConfig().getBoolean("filter.log.console", true);
    }

    public static boolean getLogLocalFileEnabled() {
        return ChatGuard.getConfig().getBoolean("filter.log.local-file", true);
    }

    public static boolean getAutoMuteEnabled() {
        return ChatGuard.getConfig().getBoolean("filter.auto-mute.enabled", true);
    }

    public static List<String> getAutoMuteDurations() {
        List<String> def = Arrays.asList("30m", "1h", "2h", "4h", "8h", "24h");
        return ChatGuard.getConfig().getStringList("filter.auto-mute.duration", def);
    }

    public static List<String> getTermsWhitelist() {
        return ChatGuard.getConfig().getStringList("filter.rules.whitelist.terms", new ArrayList<>());
    }

    public static List<String> getRegexWhitelist() {
        return ChatGuard.getConfig().getStringList("filter.rules.whitelist.regex", new ArrayList<>());
    }

    public static List<String> getTermsBlacklist() {
        return ChatGuard.getConfig().getStringList("filter.rules.blacklist.terms", new ArrayList<>());
    }

    public static List<String> getRegexBlacklist() {
        return ChatGuard.getConfig().getStringList("filter.rules.blacklist.regex", new ArrayList<>());
    }
}
