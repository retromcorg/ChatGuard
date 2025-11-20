package io.github.aleksandarharalanov.chatguard.util.auth;

public enum Permission {
    CONFIG("config", "modify the config.", true),
    NOTIFY("notify"),
    BYPASS("bypass", true),
    FILTER("filter", "set your chat filter preference."),
    STAFF("staff", "use staff commands.");

    private static final String root = "chatguard.";
    private static final String prefix = "[ChatGuard] You do not have permission to ";

    private final String subNode;
    private final String noPermissionMessage;
    private final boolean requiresStaff;

    private Permission(String subNode) {
        this(subNode, "", false);
    }

    private Permission(String subNode, boolean requiresStaff) {
        this(subNode, "", requiresStaff);
    }

    private Permission(String subNode, String noPermissionMessage) {
        this(subNode, noPermissionMessage, false);
    }

    private Permission(String subNode, String noPermissionMessage, boolean requiresStaff) {
        this.subNode = subNode;
        this.noPermissionMessage = noPermissionMessage;
        this.requiresStaff = requiresStaff;
    }

    public String noPermissionMessage() {
        return prefix + noPermissionMessage;
    }

    public boolean requiresStaff() {
        return requiresStaff;
    }

    @Override
    public String toString() {
        return root + subNode;
    }
}