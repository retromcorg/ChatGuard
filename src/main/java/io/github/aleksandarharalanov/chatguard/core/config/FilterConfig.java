package io.github.aleksandarharalanov.chatguard.core.config;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.security.common.TimeFormatter;

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

    public static boolean getStrikeDecayEnabled() {
        return ChatGuard.getConfig().getBoolean("filter.auto-mute.strike-decay.enabled", true);
    }

    public static long getStrikeDecayPeriod() {
        final String configString = ChatGuard.getConfig().getString("filter.auto-mute.strike-decay.period");

        try {
            final long futureTime = TimeFormatter.parseDateDiff(configString, true);

            return futureTime - System.currentTimeMillis();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getAutoMuteDurations() {
        List<String> def = Arrays.asList("30m", "1h", "2h", "4h", "8h", "24h");
        return ChatGuard.getConfig().getStringList("filter.auto-mute.duration", def);
    }

    public static List<String> getWhitelist() {
        return ChatGuard.getConfig().getStringList("filter.rules.whitelist", new ArrayList<>());
    }

    public static List<FilterTerm> getBlacklist() {
        List<Object> entries = ChatGuard.getConfig().getList("filter.rules.blacklist");

        List<FilterTerm> output = new ArrayList<>();
        for (Object entry : entries) {
            final FilterTerm filterTerm;

            if (entry instanceof List<?>) {
                List<?> pair = (List<?>) entry;

                final String filter = (String) pair.get(0);
                final int severity = (int) pair.get(1);

                filterTerm = new FilterTerm(filter, severity);
            }
            else if (entry instanceof String) {
                filterTerm = new FilterTerm((String) entry);
            }
            else
                throw new RuntimeException("unknown type in config for blacklist. use either (String, Int) or String.\nif confused, ask RitzKid76");

            output.add(filterTerm);
        }

        return output;
    }
}
