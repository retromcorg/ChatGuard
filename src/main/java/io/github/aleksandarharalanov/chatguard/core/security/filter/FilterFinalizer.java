package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.config.FilterTerm;
import io.github.aleksandarharalanov.chatguard.core.config.PenaltyConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogAttribute;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.core.log.logger.ConsoleLogger;
import io.github.aleksandarharalanov.chatguard.core.log.logger.DiscordLogger;
import io.github.aleksandarharalanov.chatguard.core.log.logger.FileLogger;
import io.github.aleksandarharalanov.chatguard.core.misc.AudioCuePlayer;
import io.github.aleksandarharalanov.chatguard.core.security.penalty.PenaltyEnforcer;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;

public final class FilterFinalizer {

    private FilterFinalizer() {}

    public static void finalizeActions(LogType logType, Player player, String content, FilterTerm trigger) {
        AudioCuePlayer.play(logType, player, false);
        ConsoleLogger.log(logType, player, content);
        FileLogger.log(logType, player, content);
        DiscordLogger.log(logType, player, content, trigger);
        
        if (shouldWarn(logType, player, trigger.getSeverity())) {
            PenaltyEnforcer.handleWarning(player);
            return;
        }

        if (shouldSendFeedback(logType)) {
            final String feedbackMessage = ColorUtil.translateColorCodes("&cBad words censored. Please follow the server rules");
            player.sendMessage(feedbackMessage);
        }

        PenaltyEnforcer.incrementStrikeTier(logType, player, trigger.getSeverity());
        PenaltyEnforcer.processMute(logType, player);
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
