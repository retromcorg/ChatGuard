package io.github.aleksandarharalanov.chatguard.core.log.embed;

import io.github.aleksandarharalanov.chatguard.core.config.DiscordConfig;
import io.github.aleksandarharalanov.chatguard.core.data.IPData;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.Color;

public final class NameEmbed extends DiscordEmbed {

    private final String trigger;

    public NameEmbed(JavaPlugin plugin, Player player, String content, String trigger) {
        super(plugin, player, content);
        this.trigger = trigger;
        setupBaseEmbed();
    }

    @Override
    protected void setupEmbedDetails() {
        embed.setTitle("Name Filter")
                .addField("Trigger:", String.format(DiscordConfig.getLogCensorEnabled() ? "||`%s`||" : "`%s`", trigger), true)
                .setColor(Color.decode(DiscordConfig.getEmbedColor(LogType.NAME)));
    }

    @Override
    protected String getPlayerIP() {
        return IPData.popPlayerIP(player.getName());
    }
}
