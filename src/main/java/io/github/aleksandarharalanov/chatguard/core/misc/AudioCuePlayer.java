package io.github.aleksandarharalanov.chatguard.core.misc;

import io.github.aleksandarharalanov.chatguard.core.config.MiscConfig;
import io.github.aleksandarharalanov.chatguard.core.log.LogAttribute;
import io.github.aleksandarharalanov.chatguard.core.log.LogType;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class AudioCuePlayer {

    private AudioCuePlayer() {}

    public static void play(LogType logType, Player player, boolean highPitch) {
        if (!shouldAudioCuePlay(logType)) return;

        Location location = player.getLocation();
        location.setY(location.getY() + player.getEyeHeight());

        if (highPitch) player.playEffect(location, Effect.CLICK1, 0);
        else player.playEffect(location, Effect.CLICK2, 0);
    }

    private static boolean shouldAudioCuePlay(LogType logType) {
        return MiscConfig.getAudioCuesEnabled() && logType.hasAttribute(LogAttribute.AUDIO);
    }
}
