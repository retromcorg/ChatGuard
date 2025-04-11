package io.github.aleksandarharalanov.chatguard.core.security.filter;

import io.github.aleksandarharalanov.chatguard.core.config.FilterConfig;
import io.github.aleksandarharalanov.chatguard.core.config.FilterTerm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FilterDetector {

    private FilterDetector() {}

    public static FilterTrigger checkFilters(String sanitizedContent) {
        for (FilterTerm filter : FilterConfig.getBlacklist()) {
            String regex = filter.getFilter();

            try {
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(sanitizedContent);

                if (matcher.find()) {
                    final String flaggedSection = matcher.group();

                    final String cleanedFilter = regex.replace("\\", "\\\\").replace("\"", "\\\"");
                    final FilterTerm filterTerm = new FilterTerm(filter.getName(), cleanedFilter, filter.getSeverity());

                    return new FilterTrigger(flaggedSection, filterTerm);
                }
            } catch (RuntimeException e) {
                System.out.println(String.format("[ChatGuard] Invalid regex pattern '%s' in config: %s", regex, e.getMessage()));
            }
        }
        return null;
    }
}