package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.config.FilterTerm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FilterDetector {

    private FilterDetector() {}

    public static FilterTerm checkFilters(String sanitizedContent) {
        for (FilterTerm filter : FilterConfig.getBlacklist()) { //TODO get blacklist should be cached so its not running each time someone sends a message in chat. this is not something that should run every time
            String regex = filter.getFilter();

            try {
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(sanitizedContent);
                if (matcher.find()) {
                    final String cleanedFilter = regex.replace("\\", "\\\\").replace("\"", "\\\"");
                    return new FilterTerm(cleanedFilter, filter.getSeverity());
                }
            } catch (RuntimeException e) {
                System.out.println(String.format("[ChatGuard] Invalid regex pattern '%s' in config: %s", regex, e.getMessage()));
            }
        }
        return null;
    }
}