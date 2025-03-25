package io.github.aleksandarharalanov.chatguard.core.security.penalty.plugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import io.github.aleksandarharalanov.chatguard.core.security.penalty.IMuteHandler;

public final class EssentialsMuteHandler implements IMuteHandler {

    private final Essentials essentials;

    public EssentialsMuteHandler(Essentials essentials) {
        this.essentials = essentials;
    }

    @Override
    public boolean isPlayerMuted(String playerName) {
        User user = essentials.getUser(playerName);
        return user.isMuted();
    }

    @Override
    public long getPlayerMuteTimeout(String playerName) {
        User user = essentials.getUser(playerName);
        return user.getMuteTimeout();
    }

    @Override
    public void setPlayerMuteTimeout(String playerName, long timestamp) {
        User user = essentials.getUser(playerName);
        user.setMuteTimeout(timestamp);
        user.setMuted(true);
    }
}
