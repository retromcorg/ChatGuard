package io.github.aleksandarharalanov.chatguard.core.log.embed;

import io.github.aleksandarharalanov.chatguard.core.config.DiscordConfig;
import io.github.aleksandarharalanov.chatguard.core.config.PenaltyConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.Color;

public final class ChatEmbed extends DiscordEmbed {

    private final String trigger;
    private final int severity;
    private final boolean warned;

    public ChatEmbed(JavaPlugin plugin, Player player, String content, String trigger, int severity, boolean warned) {
        super(plugin, player, content);
        this.trigger = trigger;
        this.severity = severity;
        this.warned = warned;
        setupBaseEmbed();
    }

    @Override
    protected void setupEmbedDetails() {
        if(warned) {
            embed.setDescription(String.format(
                "Warning: %d",
                PenaltyConfig.getPlayerWarnings(player) + 1
            ));
        }
        else {
            embed.setDescription(String.format(
                "Strike: %d - Mute Duration: %s",
                PenaltyConfig.getPlayerStrike(player) + severity,
                PenaltyConfig.getAutoMuteDuration(player)
            ));
        }

        embed.setTitle("Chat Filter")
                .addField("Content:", content, false)
                .addField("Trigger:", String.format(DiscordConfig.getLogCensorEnabled() ? "||`%s`||" : "`%s`", trigger), true)
                .setColor(Color.decode(DiscordConfig.getEmbedColor(LogType.CHAT)));
    }
}
