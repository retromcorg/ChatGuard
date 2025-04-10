package io.github.aleksandarharalanov.chatguard.core.config;

import io.github.aleksandarharalanov.chatguard.ChatGuard;
import org.bukkit.entity.Player;

import java.util.List;

public final class PenaltyConfig {

    private PenaltyConfig() {}

    public static List<String> getStrikesKeys() {
        return ChatGuard.getStrikes().getKeys();
    }

    public static int getPlayerStrike(String playerName) {
        return ChatGuard.getStrikes().getInt(playerName, 0);
    }

    public static int getPlayerStrike(Player player) {
        return ChatGuard.getStrikes().getInt(player.getName(), 0);
    }

    public static boolean isPlayerOnFinalStrike(Player player) {
        return ChatGuard.getStrikes().getInt(player.getName(), 0) == 5;
    }

    public static void setPlayerStrike(String playerName, int newStrike) {
        ChatGuard.getStrikes().setProperty(playerName, newStrike);
        ChatGuard.getStrikes().save();
    }

    public static void setPlayerStrike(Player player, int newStrike) {
        ChatGuard.getStrikes().setProperty(player.getName(), newStrike);
        ChatGuard.getStrikes().save();
    }

    public static void incrementPlayerStrike(Player player) {
        ChatGuard.getStrikes().setProperty(player.getName(), ChatGuard.getStrikes().getInt(player.getName(), 0) + 1);
        ChatGuard.getStrikes().save();
    }

    public static String getAutoMuteDuration(Player player) {
        final List<String> penalties = FilterConfig.getAutoMuteDurations();
        final int maxPenalty = penalties.size() - 1;

        final int playerPenalty = ChatGuard.getStrikes().getInt(player.getName(), 0);
        if (playerPenalty > maxPenalty)
            return penalties.get(maxPenalty);

        return penalties.get(playerPenalty);
    }
}
