package io.github.aleksandarharalanov.chatguard.listener.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;
import static io.github.aleksandarharalanov.chatguard.handler.spam.CommandSpamHandler.isPlayerCommandSpamming;
import static io.github.aleksandarharalanov.chatguard.util.AccessUtil.hasPermission;

public class PlayerCommandPreprocessListener extends PlayerListener {

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (hasPermission(player, "chatguard.bypass")) return;

        boolean isCommandSpamPreventionEnabled = getConfig().getBoolean("spam-prevention.enabled.command", true);
        if (isCommandSpamPreventionEnabled)
            if (isPlayerCommandSpamming(player))
                event.setCancelled(true);
    }
}
