package io.github.aleksandarharalanov.chatguard.core.config;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import io.github.aleksandarharalanov.chatguard.util.config.ConfigUtil;

import org.bukkit.entity.Player;

import java.util.List;

public final class PenaltyConfig {

    private PenaltyConfig() {}

    public static List<String> getStrikesKeys() {
        return ChatGuard.getStrikes().getKeys();
    }

    public static int getPlayerStrike(Player player) {
        return getPlayerStrike(player.getName());
    }

    public static int getPlayerStrike(String playerName) {
        return ChatGuard.getStrikes().getInt(playerName + ".strikes", 0);
    }

    public static int getPlayerWarnings(Player player) {
        return getPlayerWarnings(player.getName());
    }

    public static int getPlayerWarnings(String playerName) {
        return ChatGuard.getStrikes().getInt(playerName + ".warnings", 0);
    }

    public static void setPlayerStrike(Player player, int newStrike) {
        setPlayerStrike(player.getName(), newStrike);
    }

    public static void decrementPlayerStrike(Player player, int amount, long updateTime) {
        setPlayerStrike(player.getName(), getPlayerStrike(player) - amount, updateTime);
    }

    public static void incrementPlayerStrike(Player player, int amount) {
        setPlayerStrike(player, getPlayerStrike(player) + amount);
    }

    public static void setPlayerStrike(String playerName, int newStrike) {
        setPlayerStrike(playerName, newStrike, System.currentTimeMillis());
    }

    public static void setPlayerStrike(String playerName, int newStrike, long updateTime) {
        ConfigUtil strikes = ChatGuard.getStrikes();

        newStrike = Math.max(newStrike, 0);

        if(newStrike == 0 && getPlayerWarnings(playerName) <= 0)
            strikes.removeProperty(playerName);
        else {
            strikes.setProperty(playerName + ".strikes", newStrike);
            strikes.setProperty(playerName + ".updated", updateTime);
        }

        ChatGuard.getStrikes().save();
    }

    public static void incrementPlayerWarnings(Player player) {
        setPlayerWarnings(player, getPlayerWarnings(player) + 1);
    }

    public static void setPlayerWarnings(Player player, int warnings) {
        setPlayerWarnings(player.getName(), warnings);
    }

    public static void setPlayerWarnings(String playerName, int warnings) {
        ChatGuard.getStrikes().setProperty(playerName + ".warnings", warnings);
    }

    public static String getAutoMuteDuration(Player player) {
        final List<String> penalties = FilterConfig.getAutoMuteDurations();
        final int maxPenalty = penalties.size() - 1;

        int playerPenalty = getPlayerStrike(player);

        if (playerPenalty > maxPenalty)
            return penalties.get(maxPenalty);

        return penalties.get(playerPenalty);
    }

    public static long getLastMuteTime(Player player) {
        return getLastMuteTime(player.getName());
    }
    
    public static long getLastMuteTime(String playerName) {
        final String lastUpdatedString = ChatGuard.getStrikes().getString(playerName + ".updated");
        if(lastUpdatedString == null)
            return -1;

        try {
            return Long.parseLong(lastUpdatedString);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
