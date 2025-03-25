package io.github.aleksandarharalanov.chatguard.core.log.embed;

import io.github.aleksandarharalanov.chatguard.core.config.DiscordConfig;
import io.github.aleksandarharalanov.chatguard.core.config.PenaltyConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.Color;

public final class SignEmbed extends DiscordEmbed {

    private final String trigger;

    public SignEmbed(JavaPlugin plugin, Player player, String content, String trigger) {
        super(plugin, player, content);
        this.trigger = trigger;
        setupBaseEmbed();
    }

    @Override
    protected void setupEmbedDetails() {
        if (PenaltyConfig.isPlayerOnFinalStrike(player)) {
            embed.setDescription(String.format(
                    "S%d (Max)",
                    PenaltyConfig.getPlayerStrike(player)
            ));
        } else {
            embed.setDescription(String.format(
                    "S%d ► S%d",
                    PenaltyConfig.getPlayerStrike(player),
                    PenaltyConfig.getPlayerStrike(player) + 1
            ));
        }

        embed.setTitle("Sign Filter")
                .addField("Content:", content, false)
                .addField("Trigger:", String.format(DiscordConfig.getLogCensorEnabled() ? "||`%s`||" : "`%s`", trigger), true)
                .setColor(Color.decode(DiscordConfig.getEmbedColor(LogType.SIGN)));
    }
}
