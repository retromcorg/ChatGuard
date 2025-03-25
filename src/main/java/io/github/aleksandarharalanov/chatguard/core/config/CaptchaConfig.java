package io.github.aleksandarharalanov.chatguard.core.config;

import io.github.aleksandarharalanov.chatguard.ChatGuard;

import java.util.ArrayList;
import java.util.List;

public final class CaptchaConfig {

    private CaptchaConfig() {}

    public static boolean getEnabled() {
        return ChatGuard.getConfig().getBoolean("captcha.enabled", true);
    }

    public static int getThreshold() {
        return ChatGuard.getConfig().getInt("captcha.threshold", 5);
    }

    public static String getCodeCharacters() {
        return ChatGuard.getConfig().getString("captcha.code.characters", "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789");
    }

    public static int getCodeLength() {
        return ChatGuard.getConfig().getInt("captcha.code.length", 5);
    }

    public static boolean getLogConsoleEnabled() {
        return ChatGuard.getConfig().getBoolean("captcha.log-console", true);
    }

    public static List<String> getTermsWhitelist() {
        return ChatGuard.getConfig().getStringList("captcha.rules-whitelist.terms", new ArrayList<>());
    }

    public static List<String> getRegexWhitelist() {
        return ChatGuard.getConfig().getStringList("captcha.rules-whitelist.regex", new ArrayList<>());
    }

    public static String getPlayerCaptcha(String playerName) {
        return ChatGuard.getCaptchas().getString(playerName);
    }

    public static void removePlayerCaptcha(String playerName) {
        ChatGuard.getCaptchas().removeProperty(playerName);
        ChatGuard.getCaptchas().save();
    }

    public static void setPlayerCaptcha(String playerName, String captchaCode) {
        ChatGuard.getCaptchas().setProperty(playerName, captchaCode);
        ChatGuard.getCaptchas().save();
    }
}
