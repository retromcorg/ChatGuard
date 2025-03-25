package io.github.aleksandarharalanov.chatguard.core.security.penalty;

public interface IMuteHandler {

    boolean isPlayerMuted(String playerName);

    long getPlayerMuteTimeout(String playerName);

    void setPlayerMuteTimeout(String playerName, long timestamp);
}
