package io.github.aleksandarharalanov.chatguard.listener.player;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.security.filter.FilterHandler;
import io.github.aleksandarharalanov.chatguard.core.security.filter.FilterTrigger;
import io.github.aleksandarharalanov.chatguard.core.security.penalty.PenaltyEnforcer;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerJoinListener extends PlayerListener {

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Fallback if PlayerLoginEvent fails
        if (FilterConfig.getNameEnabled() && FilterHandler.wasPlayerNameBlocked(player)) {
            // this is pretty garbage, but i cant be asked to do this properly...
            FilterTrigger trigger = FilterHandler.GetBlockedTrigger(player.getName());
            String filterName = trigger.getFilterTerm().getName();

            player.kickPlayer(ColorUtil.translateColorCodes(
                String.format(
                    "&cYour name contains the bad word '%s'.",
                    filterName
                )
            ));
            return;
        }

        PenaltyEnforcer.updatePlayerData(player);
    }
}
