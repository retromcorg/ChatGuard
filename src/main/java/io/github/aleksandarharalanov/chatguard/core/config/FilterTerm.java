package io.github.aleksandarharalanov.chatguard.core.config;

public class FilterTerm {
    private String name;
    private String filter;

    private int severity = 1;

    public FilterTerm(String name, String filter) {
        this.name = name;
        this.filter = filter;
    }
    
    public FilterTerm(String name, String filter, int severity) {
        this(name, filter);

        this.severity = severity;
    }

    public String getName() {
        return name;
    }

    public String getFilter() {
        return filter;
    }

    public int getSeverity() {
        return severity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if(o == null)
            return false;

        if(o instanceof String)
            return equalsString((String) o);
        if(o instanceof FilterTerm)
            return equalsFilterTerm((FilterTerm) o);

        return false;
    }

    private boolean equalsString(String s) {
        final FilterTerm that = new FilterTerm("", s);
        return equalsFilterTerm(that);
    }

    private boolean equalsFilterTerm(FilterTerm that) {
        return filter == that.getFilter();
    }
}
