package io.github.aleksandarharalanov.chatguard.util.auth;

public enum Permission {
    CONFIG("config"),
    NOTIFY("notify"),
    BYPASS("bypass");

    private static final String root = "chatguard.";
    private final String subNode;

    private Permission(String subNode) {
        this.subNode = subNode;
    }

    @Override
    public String toString() {
        return root + subNode;
    }
}