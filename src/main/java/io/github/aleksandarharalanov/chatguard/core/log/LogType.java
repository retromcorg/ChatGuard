package io.github.aleksandarharalanov.chatguard.core.log;

import java.util.EnumSet;

public enum LogType {

    CHAT(EnumSet.of(LogAttribute.FILTER, LogAttribute.STRIKE, LogAttribute.MUTE, LogAttribute.WARN, LogAttribute.AUDIO)),
    SIGN(EnumSet.of(LogAttribute.FILTER, LogAttribute.STRIKE, LogAttribute.WARN, LogAttribute.AUDIO)),
    NAME(EnumSet.of(LogAttribute.FILTER)),
    CAPTCHA(EnumSet.of(LogAttribute.AUDIO));

    private final EnumSet<LogAttribute> attributes;

    LogType(EnumSet<LogAttribute> attributes) {
        this.attributes = attributes;
    }

    public boolean hasAttribute(LogAttribute attribute) {
        return this.attributes.contains(attribute);
    }

    public String getName() {
        return this.name().toLowerCase();
    }
}