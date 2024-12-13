package io.github.aleksandarharalanov.chatguard.listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getStrikes;

public class PlayerJoinListener extends PlayerListener {
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerUsername = event.getPlayer().getName();
        if (getStrikes().getInt(playerUsername, -1) == -1) {
            getStrikes().setProperty(playerUsername, 0);
            getStrikes().saveConfig();
        }
    }
}
