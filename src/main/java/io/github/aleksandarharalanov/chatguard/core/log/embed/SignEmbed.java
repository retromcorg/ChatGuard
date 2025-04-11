package io.github.aleksandarharalanov.chatguard.core.log.embed;

import io.github.aleksandarharalanov.chatguard.core.config.DiscordConfig;
import io.github.aleksandarharalanov.chatguard.core.config.PenaltyConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.Color;

public final class SignEmbed extends DiscordEmbed {

    private final String trigger;
    private final int severity;

    public SignEmbed(JavaPlugin plugin, Player player, String content, String trigger, int severity) {
        super(plugin, player, content);
        this.trigger = trigger;
        this.severity = severity;
        setupBaseEmbed();
    }

    @Override
    protected void setupEmbedDetails() {
        embed.setDescription(String.format(
            "Strike: %d",
            PenaltyConfig.getPlayerStrike(player) + 1
        ));

        embed.setTitle("Sign Filter")
                .addField("Content:", content, false)
                .addField("Trigger:", String.format(DiscordConfig.getLogCensorEnabled() ? "||`%s`||" : "`%s`", trigger), true)
                .setColor(Color.decode(DiscordConfig.getEmbedColor(LogType.SIGN)));
    }
}
