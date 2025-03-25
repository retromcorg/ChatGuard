package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.util.log.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FilterDetector {

    private FilterDetector() {}

    public static String getTrigger(String sanitizedContent) {
        String trigger = checkBlacklistedTerms(sanitizedContent);
        return (trigger != null) ? trigger : checkRegexPatterns(sanitizedContent);
    }

    private static String checkBlacklistedTerms(String sanitizedContent) {
        String[] contentTerms = sanitizedContent.split("\\s+");
        for (String term : contentTerms) {
            if (FilterConfig.getTermsBlacklist().contains(term)) {
                return term;
            }
        }
        return null;
    }

    private static String checkRegexPatterns(String sanitizedContent) {
        for (String regex : FilterConfig.getRegexBlacklist()) {
            try {
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(sanitizedContent);
                if (matcher.find()) {
                    return regex.replace("\\", "\\\\").replace("\"", "\\\"");
                }
            } catch (RuntimeException e) {
                LogUtil.logConsoleWarning(String.format("[ChatGuard] Invalid regex pattern '%s' in config: %s", regex, e.getMessage()));
            }
        }
        return null;
    }
}