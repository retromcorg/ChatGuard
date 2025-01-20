package io.github.aleksandarharalanov.chatguard.handler;

import org.bukkit.entity.Player;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.ChatGuard.getStrikes;

public class PunishmentHandler {

    public static int getStrike(String playerName) {
        return getStrikes().getInt(playerName, 0);
    }

    public static int getStrike(Player player) {
        return getStrikes().getInt(player.getName(), 0);
    }

    public static String getMuteDuration(String playerName) {
        return getConfig().getString(String.format("filter.mute.duration.s%d", getStrikes().getInt(playerName, 0)));
    }

    public static String getMuteDuration(Player player) {
        return getConfig().getString(String.format("filter.mute.duration.s%d", getStrikes().getInt(player.getName(), 0)));
    }
}
