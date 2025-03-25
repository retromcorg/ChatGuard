package io.github.aleksandarharalanov.chatguard.core.security.penalty.plugin;

import io.github.aleksandarharalanov.chatguard.core.security.penalty.IMuteHandler;
import me.zavdav.zcore.api.Punishments;
import me.zavdav.zcore.data.Mute;
import me.zavdav.zcore.util.PlayerUtils;

import java.util.UUID;

public final class ZCoreMuteHandler implements IMuteHandler {

    @Override
    public boolean isPlayerMuted(String playerName) {
        return Punishments.isPlayerMuted(PlayerUtils.getUUIDFromUsername(playerName));
    }

    @Override
    public long getPlayerMuteTimeout(String playerName) {
        UUID uuid = PlayerUtils.getUUIDFromUsername(playerName);
        if (!Punishments.isPlayerMuted(uuid)) {
            return System.currentTimeMillis();
        }

        Mute mute = Punishments.getMute(uuid);

        // It is possible that getDuration() returns null, which specifies a permanent mute.
        // In this case, return 100 years as the timeout.
        if (mute.getDuration() == null) {
            return 31_536_000L * 1000L * 100L;
        } else {
            return mute.getDuration() * 1000L;
        }
    }

    @Override
    public void setPlayerMuteTimeout(String playerName, long timestamp) {
        Punishments.mutePlayer(PlayerUtils.getUUIDFromUsername(playerName),
                null,
                (timestamp - System.currentTimeMillis()) / 1000L,
                "Bad words in chat message"
        );
    }
}
