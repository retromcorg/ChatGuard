package io.github.aleksandarharalanov.chatguard.core.security.penalty;

import com.earth2me.essentials.Essentials;
import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.config.PenaltyConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogAttribute;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import io.github.aleksandarharalanov.chatguard.core.security.common.TimeFormatter;
import io.github.aleksandarharalanov.chatguard.core.security.penalty.plugin.EssentialsMuteHandler;
import io.github.aleksandarharalanov.chatguard.util.auth.AccessUtil;
import io.github.aleksandarharalanov.chatguard.util.auth.Permission;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public final class PenaltyEnforcer {

    private static final IMuteHandler muteHandler;

    static {
        PluginManager pM = Bukkit.getServer().getPluginManager();
        if (pM.getPlugin("Essentials") != null) {
            muteHandler = new EssentialsMuteHandler((Essentials) pM.getPlugin("Essentials"));
        } else {
            muteHandler = null;
        }
    }

    public static void processMute(LogType logType, Player player) {
        if (muteHandler == null) {
            System.out.println("[ChatGuard] No compatible plugin found for auto mute feature. Please disable in config.");
            return;
        }

        if (!FilterConfig.getAutoMuteEnabled()) {
            return;
        }

        if (!logType.hasAttribute(LogAttribute.MUTE)) {
            return;
        }

        try {
            String duration = PenaltyConfig.getAutoMuteDuration(player);

            long timeStamp = TimeFormatter.parseDateDiff(duration, true);

            muteHandler.setPlayerMuteTimeout(player.getName(), timeStamp);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        notifyPlayers(player.getName(), PenaltyConfig.getAutoMuteDuration(player));
    }

    private static void notifyPlayers(String playerName, String muteDuration)
    {
        String message = String.format(
                "&c[ChatGuard] %s muted for %s. by system; content has bad words.",
                playerName, muteDuration
        );

        for (Player player : Bukkit.getServer().getOnlinePlayers())
            if (AccessUtil.senderHasPermission(player, Permission.NOTIFY).result())
                player.sendMessage(message);
    }

    public static void incrementStrikeTier(LogType logType, Player player, int severity) {
        if (!logType.hasAttribute(LogAttribute.STRIKE)) {
            return;
        }

        PenaltyConfig.incrementPlayerStrike(player, severity);
    }

    public static IMuteHandler getMuteHandler() {
        return muteHandler;
    }

    // TODO this needs to also run on plugin start up to clean up player data of those who havent joined the server in a while
    // not sure how that would work with the update messages sent to the player unless its now a new entry in their yml. yikes...
    public static void updatePlayerData(Player player) {
        if(FilterConfig.getStrikeDecayEnabled())
            updatePlayerStrikes(player);
        if(FilterConfig.getWarningDecayEnabled())
            updatePlayerWarnings(player);
    }

    private static void updatePlayerStrikes(Player player) {
        final int playerStrikeCount = PenaltyConfig.getPlayerStrikes(player);
        if(playerStrikeCount <= 0)
            return; // player has no strikes

        final long lastMuteTime = PenaltyConfig.getLastMuteTime(player);

        final long timePassed = System.currentTimeMillis() - lastMuteTime;
        final long decayPeriod = FilterConfig.getStrikeDecayPeriod();

        final int strikesToRevoke = Math.min(
            (int) (timePassed / decayPeriod),
            playerStrikeCount
        );

        // this is only really done to prevent longer decay times than would normally happen under the config
        final long totalRevokePeriod = strikesToRevoke * decayPeriod;
        final long newPlayerUpdatedTime = lastMuteTime + totalRevokePeriod;

        PenaltyConfig.decrementPlayerStrike(player, strikesToRevoke, newPlayerUpdatedTime);

        String pointsString = "point";
        if(strikesToRevoke != 1)
            pointsString += "s";

        final String coloredMessage = ColorUtil.translateColorCodes(String.format("&aYour ChatGuard strike count has been reduced by %d %s! You have %d remaining", strikesToRevoke, pointsString, playerStrikeCount - strikesToRevoke));
        player.sendMessage(coloredMessage);
    }

    private static void updatePlayerWarnings(Player player) {
        final int playerWarningCount = PenaltyConfig.getPlayerWarnings(player);
        if(playerWarningCount <= 0)
            return; // player has no warnings

        final long lastWarnTime = PenaltyConfig.getLastWarnTime(player);

        final long timePassed = System.currentTimeMillis() - lastWarnTime;
        final long decayPeriod = FilterConfig.getWarningDecayPeriod();

        final int warningsToRevoke = Math.min(
            (int) (timePassed / decayPeriod),
            playerWarningCount
        );

        // this is only really done to prevent longer decay times than would normally happen under the config
        final long totalRevokePeriod = warningsToRevoke * decayPeriod;
        final long newPlayerUpdatedTime = lastWarnTime + totalRevokePeriod;

        PenaltyConfig.decrementPlayerWarning(player, warningsToRevoke, newPlayerUpdatedTime);

        String pointsString = "point";
        if(warningsToRevoke != 1)
            pointsString += "s";

        final String coloredMessage = ColorUtil.translateColorCodes(String.format("&aYour ChatGuard warning count has been reduced by %d %s! You have %d remaining", warningsToRevoke, pointsString, playerWarningCount - warningsToRevoke));
        player.sendMessage(coloredMessage);
    }

    public static void handleWarning(Player player) {
        PenaltyConfig.incrementPlayerWarnings(player);

        final String coloredMessage = ColorUtil.translateColorCodes("&cWarning number: " + PenaltyConfig.getPlayerWarnings(player));
        player.sendMessage(coloredMessage);
    }
}
