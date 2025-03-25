package io.github.aleksandarharalanov.chatguard.listener.player;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.security.filter.FilterHandler;
import io.github.aleksandarharalanov.chatguard.core.config.PenaltyConfig;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerJoinListener extends PlayerListener {

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Fallback if PlayerLoginEvent fails
        if (FilterConfig.getNameEnabled() && FilterHandler.isPlayerNameBlocked(player)) {
            player.kickPlayer(ColorUtil.translateColorCodes("&cName contains bad words."));
            return;
        }

        PenaltyConfig.setDefaultStrikeTier(player);
    }
}
