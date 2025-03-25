package io.github.aleksandarharalanov.chatguard.core.security.captcha;

import io.github.aleksandarharalanov.chatguard.core.config.CaptchaConfig;

import java.util.Random;

public final class CaptchaGenerator {

    private CaptchaGenerator() {}

    public static String generateCaptchaCode() {
        StringBuilder captcha = new StringBuilder(CaptchaConfig.getCodeLength());
        Random random = new Random();

        for (int i = 0; i < CaptchaConfig.getCodeLength(); i++) {
            captcha.append(CaptchaConfig.getCodeCharacters()
                    .charAt(random.nextInt(CaptchaConfig.getCodeCharacters().length())));
        }

        return captcha.toString();
    }
}
