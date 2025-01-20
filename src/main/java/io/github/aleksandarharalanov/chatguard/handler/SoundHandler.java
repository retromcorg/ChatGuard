package io.github.aleksandarharalanov.chatguard.handler;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;

public class SoundHandler {

    public static void playSoundCue(Player player, boolean pitch) {
        boolean isSoundCuesEnabled = getConfig().getBoolean("miscellaneous.sound-cues", true);
        if (isSoundCuesEnabled) {
            Location location = player.getLocation();
            location.setY(location.getY() + player.getEyeHeight());
            if (pitch) player.playEffect(location, Effect.CLICK1, 0);
            else player.playEffect(location, Effect.CLICK2, 0);
        }
    }
}
