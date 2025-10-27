package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.core.security.common.ContentHandler;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

public final class FilterHandler {

    private FilterHandler() {}

    public static boolean wasChatContentBlocked(Player player, String content) {
        return wasBlocked(LogType.CHAT, player, content, null);
    }

    public static boolean wasSignContentBlocked(Player player, SignChangeEvent event) {
        String[] content = event.getLines();
        Location eventLocation = event.getBlock().getLocation();

        return wasBlocked(LogType.SIGN, player, ContentHandler.mergeContent(content), eventLocation);
    }

    public static boolean wasPlayerNameBlocked(Player player) {
        return wasBlocked(LogType.NAME, player, player.getName(), null);
    }

    private static boolean wasBlocked(LogType logType, Player player, String content, Location violationLocation) {
        FilterTrigger trigger = GetBlockedTrigger(content);

        if (trigger == null)
            return false;

        trigger.setViolationLocation(violationLocation);

        FilterFinalizer.finalizeActions(logType, player, content, trigger);
        return true;
    }

    public static FilterTrigger GetBlockedTrigger(String content) {
        String sanitizedContent = ContentHandler.sanitizeContent(content, FilterConfig.getWhitelist());
        FilterTrigger trigger = FilterDetector.checkFilters(sanitizedContent);

        if (trigger == null)
            return null;

        return trigger;
    }
}
