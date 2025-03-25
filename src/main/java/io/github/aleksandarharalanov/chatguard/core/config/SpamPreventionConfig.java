package io.github.aleksandarharalanov.chatguard.core.config;

import io.github.aleksandarharalanov.chatguard.ChatGuard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SpamPreventionConfig {

    private SpamPreventionConfig() {}

    public static boolean getChatEnabled() {
        return ChatGuard.getConfig().getBoolean("spam-prevention.enabled.chat", true);
    }

    public static boolean getCommandEnabled() {
        return ChatGuard.getConfig().getBoolean("spam-prevention.enabled.command", true);
    }

    public static boolean getWarnPlayerEnabled() {
        return ChatGuard.getConfig().getBoolean("spam-prevention.warn-player", true);
    }

    public static List<Integer> getCooldownMsChat() {
        List<Integer> def = Arrays.asList(1000, 2000, 3000, 4000, 5000, 6000);
        return ChatGuard.getConfig().getIntList("spam-prevention.cooldown-ms.chat", def);
    }

    public static List<Integer> getCooldownMsCommand() {
        List<Integer> def = Arrays.asList(5000, 7500, 10000, 12500, 15000, 17500);
        return ChatGuard.getConfig().getIntList("spam-prevention.cooldown-ms.command", def);
    }

    public static List<String> getCommandWhitelist() {
        return ChatGuard.getConfig().getStringList("spam-prevention.rules-whitelist.command", new ArrayList<>());
    }
}
