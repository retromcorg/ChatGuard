package io.github.aleksandarharalanov.chatguard.core.config;

import io.github.aleksandarharalanov.chatguard.ChatGuard;

public final class MiscConfig {

    private MiscConfig() {}

    public static boolean getAudioCuesEnabled() {
        return ChatGuard.getConfig().getBoolean("miscellaneous.audio-cues", true);
    }
}
