package io.github.aleksandarharalanov.chatguard.listener.player;

import io.github.aleksandarharalanov.chatguard.core.security.filter.FilterHandler;
import io.github.aleksandarharalanov.chatguard.core.security.filter.FilterTrigger;
import io.github.aleksandarharalanov.chatguard.core.data.IPData;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener extends PlayerListener {

    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            IPData.storePlayerIP(player.getName(), event.getKickMessage());
        }

        if (FilterHandler.wasPlayerNameBlocked(player)) {
            // this is pretty garbage, but i cant be asked to do this properly...
            FilterTrigger trigger = FilterHandler.GetBlockedTrigger(player.getName());
            String filterName = trigger.getFilterTerm().getName();

            event.disallow(
                PlayerLoginEvent.Result.KICK_OTHER,
                ColorUtil.translateColorCodes(
                    String.format(
                        "&cYour name contains the bad word '%s'.",
                        filterName
                    )
                )
            );
        }
    }
}
