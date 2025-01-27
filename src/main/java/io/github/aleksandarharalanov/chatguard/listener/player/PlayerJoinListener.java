package io.github.aleksandarharalanov.chatguard.listener.player;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getStrikes;

public class PlayerJoinListener extends PlayerListener {

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        if (getStrikes().getInt(playerName, -1) == -1) {
            getStrikes().setProperty(playerName, 0);
            getStrikes().saveConfig();
        }
    }
}
