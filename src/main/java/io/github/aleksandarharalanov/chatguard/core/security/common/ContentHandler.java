package io.github.aleksandarharalanov.chatguard.core.security.common;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ContentHandler {

    private ContentHandler() {}

    public static String sanitizeContent(String content, List<String> termsWhitelist, List<String> regexWhitelist) {
        String sanitizedContent = content.toLowerCase();

        for (String term : termsWhitelist) {
            sanitizedContent = sanitizedContent.replaceAll("\\b" + Pattern.quote(term) + "\\b", "");
        }

        for (String regex : regexWhitelist) {
            try {
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(sanitizedContent);
                sanitizedContent = matcher.replaceAll("");
            } catch (RuntimeException e) {
                System.out.println(String.format("[ChatGuard] Invalid regex pattern '%s' in config: %s", regex, e.getMessage()));
            }
        }

        return sanitizedContent.trim();
    }

    public static String sanitizeContent(String content, List<String> whiteList) {
        String sanitizedContent = content.toLowerCase();

        for (String regex : whiteList) {
            try {
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(sanitizedContent);
                sanitizedContent = matcher.replaceAll("");
            } catch (RuntimeException e) {
                System.out.println(String.format("[ChatGuard] Invalid regex pattern '%s' in config: %s", regex, e.getMessage()));
            }
        }

        return sanitizedContent.trim();
    }

    public static String mergeContent(String[] content) {
        return Stream.of(content)
                .map(String::toLowerCase)
                .collect(Collectors.joining(" "));
    }
}
