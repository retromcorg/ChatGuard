package io.github.aleksandarharalanov.chatguard.core.log.embed;

import io.github.aleksandarharalanov.chatguard.core.config.CaptchaConfig;
import io.github.aleksandarharalanov.chatguard.core.config.DiscordConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.Color;

public final class CaptchaEmbed extends DiscordEmbed {

    public CaptchaEmbed(JavaPlugin plugin, Player player, String content) {
        super(plugin, player, content);
        setupBaseEmbed();
    }

    @Override
    protected void setupEmbedDetails() {
        embed.setTitle("Captcha Trigger")
                .setDescription(String.format("Repeated Content ・ %dx", CaptchaConfig.getThreshold()))
                .addField("Content:", content, false)
                .setColor(Color.decode(DiscordConfig.getEmbedColor(LogType.CAPTCHA)));
    }
}
