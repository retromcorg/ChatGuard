package io.github.aleksandarharalanov.chatguard.core.log.logger;

import org.bukkit.entity.Player;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogAttribute;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;

public final class ConsoleLogger {

    private ConsoleLogger() {}

    public static void log(LogType logType, Player player, String content) {
        if (!shouldConsoleLogEnabled(logType))
            return;

        String logMessage = String.format("[ChatGuard] [%s]", logType.name());
        switch (logType) {
            case CHAT:
                logMessage = String.format("%s Stopped player '%s'; Bad content: '%s'", logMessage, player.getName(),
                        content);
                break;
            case SIGN:
                logMessage = String.format("%s Stopped player '%s'; Bad sign: '%s'", logMessage, player.getName(),
                        content);
                break;
            case NAME:
                logMessage = String.format("%s Stopped player '%s'; Bad name.", logMessage, content);
                break;
            default:
                return;
        }

        System.out.println(logMessage);
    }

    private static boolean shouldConsoleLogEnabled(LogType logType) {
        if (logType.hasAttribute(LogAttribute.FILTER))
            return FilterConfig.getLogConsoleEnabled();

        return false;
    }
}
