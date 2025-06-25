package io.github.aleksandarharalanov.chatguard.core.config;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.core.security.common.TimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FilterConfig {

    private static List<FilterTerm> blacklist;

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

    public static boolean getWarningDecayEnabled() {
        return ChatGuard.getConfig().getBoolean("filter.auto-mute.warning-decay.enabled", true);
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

    public static long getWarningDecayPeriod() {
        final String configString = ChatGuard.getConfig().getString("filter.auto-mute.warning-decay.period");

        try {
            final long futureTime = TimeFormatter.parseDateDiff(configString, true);

            return futureTime - System.currentTimeMillis();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int getWarningcount() {
        return ChatGuard.getConfig().getInt("filter.auto-mute.warnings.warning-count", 0);
    }

    public static int getWarningBypassThreashold() {
        return ChatGuard.getConfig().getInt("filter.auto-mute.warnings.severity-bypass-threashold", 3);
    }

    public static List<String> getAutoMuteDurations() {
        List<String> def = Arrays.asList("30m", "1h", "2h", "4h", "8h", "24h");
        return ChatGuard.getConfig().getStringList("filter.auto-mute.duration", def);
    }

    public static List<String> getWhitelist() {
        return ChatGuard.getConfig().getStringList("filter.rules.whitelist", new ArrayList<>());
    }

    public static List<FilterTerm> getBlacklist() {
        if (blacklist == null)
            generateBlackListCache();

        return blacklist;
    }

    public static void generateBlackListCache() {
        List<Object> entries = ChatGuard.getConfig().getList("filter.rules.blacklist");

        List<FilterTerm> newBlackList = new ArrayList<>();

        try {
            for (Object entry : entries) {
                final FilterTerm filterTerm;

                List<?> pair = (List<?>) entry;
                if (pair.size() == 2) {
                    final String name = (String) pair.get(0);
                    final String filter = (String) pair.get(1);

                    filterTerm = new FilterTerm(name, filter);
                } else {
                    final String name = (String) pair.get(0);
                    final String filter = (String) pair.get(1);

                    final int severity = (Integer) pair.get(2);

                    filterTerm = new FilterTerm(name, filter, severity);
                }

                newBlackList.add(filterTerm);
            }

            blacklist = newBlackList;
        }
        catch(
            ClassCastException e
        ) {
            throw new RuntimeException("unknown type in config for blacklist. use either (String, String, Int) or (String, String). if confused, ask RitzKid76");
        }
    }
}
