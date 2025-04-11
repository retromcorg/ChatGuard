package io.github.aleksandarharalanov.chatguard.core.config;

public class FilterTerm {
    private String filter = "";
    private int severity = 1;

    public FilterTerm(String filter) {
        this.filter = filter;
    }

    public FilterTerm(String filter, int severity) {
        this.filter = filter;
        this.severity = severity;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
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
        final FilterTerm that = new FilterTerm(s, 1);
        return equalsFilterTerm(that);
    }

    private boolean equalsFilterTerm(FilterTerm that) {
        return filter == that.getFilter();
    }
}
