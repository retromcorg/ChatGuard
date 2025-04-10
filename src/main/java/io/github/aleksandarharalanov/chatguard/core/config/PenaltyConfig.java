package io.github.aleksandarharalanov.chatguard.core.config;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
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

    public static void setPlayerStrike(Player player, int newStrike) {
        setPlayerStrike(player.getName(), newStrike);
    }

    public static void incrementPlayerStrike(Player player) {
        setPlayerStrike(player, getPlayerStrike(player) + 1);
    }

    public static void setPlayerStrike(String playerName, int newStrike) {
        ChatGuard.getStrikes().setProperty(playerName + ".strikes", newStrike);
        ChatGuard.getStrikes().save();
    }

    public static String getAutoMuteDuration(Player player) {
        final List<String> penalties = FilterConfig.getAutoMuteDurations();
        final int maxPenalty = penalties.size() - 1;

        int playerPenalty = getPlayerStrike(player);

        if (playerPenalty > maxPenalty)
            return penalties.get(maxPenalty);

        return penalties.get(playerPenalty);
    }
}
