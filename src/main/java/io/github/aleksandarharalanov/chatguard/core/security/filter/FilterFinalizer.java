package io.github.aleksandarharalanov.chatguard.core.security.filter;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.config.FilterTerm;
import io.github.aleksandarharalanov.chatguard.core.config.PenaltyConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogAttribute;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.core.log.logger.ConsoleLogger;
import io.github.aleksandarharalanov.chatguard.core.log.logger.DiscordLogger;
import io.github.aleksandarharalanov.chatguard.core.log.logger.FileLogger;
import io.github.aleksandarharalanov.chatguard.core.security.penalty.PenaltyEnforcer;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;

public final class FilterFinalizer {

    private FilterFinalizer() {}

    public static void finalizeActions(LogType logType, Player player, String content, FilterTrigger trigger) {
        final FilterTerm filterTerm = trigger.getFilterTerm();
        final int severity = filterTerm.getSeverity();
        final boolean shouldWarn = shouldWarn(logType, player, severity);
        final Location violationLocation = trigger.getViolationLocation();

        if (shouldSendFeedback(logType)) {
            final String badWord = filterTerm.getName();
            final String flaggedSection = trigger.getSection();

            player.sendMessage(ColorUtil.translateColorCodes(
                String.format("&cYour message contains the flagged word: '%s'", badWord)
            ));
            player.sendMessage(ColorUtil.translateColorCodes(
                String.format("&cSection flagged: '%s'", flaggedSection)
            ));
        }

        if (shouldWarn) {
            PenaltyEnforcer.handleWarning(player);
        } else {
            PenaltyEnforcer.incrementStrikeTier(logType, player, severity);
            PenaltyEnforcer.processMute(logType, player);
        }

        ConsoleLogger.log(logType, player, content);
        FileLogger.log(logType, player, content);
        DiscordLogger.log(logType, player, content, filterTerm, shouldWarn, violationLocation);
    }

    private static boolean shouldWarn(LogType logType, Player player, int severity) {
        if (!logType.hasAttribute(LogAttribute.MUTE))
            return false;

        final int warningBypassThreashold = FilterConfig.getWarningBypassThreashold();
        if(severity >= warningBypassThreashold)
            return false;
            
        final int playerWarnings = PenaltyConfig.getPlayerWarnings(player);
        final int maxWarnings = FilterConfig.getWarningcount();
        if(playerWarnings >= maxWarnings)
            return false;

        return true;
    }

    private static boolean shouldSendFeedback(LogType logType) {
        return logType.hasAttribute(LogAttribute.WARN) && FilterConfig.getWarnPlayerEnabled();
    }
}
