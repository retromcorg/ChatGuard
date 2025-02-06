package io.github.aleksandarharalanov.chatguard.listener.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPreLoginEvent;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getStrikes;
import static io.github.aleksandarharalanov.chatguard.handler.FilterHandler.shouldBlockUsername;
import static io.github.aleksandarharalanov.chatguard.util.ColorUtil.translate;
import static io.github.aleksandarharalanov.chatguard.util.LoggerUtil.logWarning;

public class PlayerJoinListener extends PlayerListener {

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (shouldBlockUsername(player.getName())) {
            player.kickPlayer(translate("&cKICKED: Username contains blocked words."));
            logWarning(String.format("[ChatGuard] Kicked player '%s' for bad username.", player.getName()));
            return;
        }

        if (getStrikes().getInt(player.getName(), -1) == -1) {
            getStrikes().setProperty(player.getName(), 0);
            getStrikes().save();
        }
    }

    @Override
    public void onPlayerPreLogin(PlayerPreLoginEvent event) {
        String playerName = event.getName();
        if (shouldBlockUsername(playerName))
            event.disallow(
                    PlayerPreLoginEvent.Result.KICK_BANNED,
                    translate("&cKICKED: Username contains blocked words.")
            );
    }
}
