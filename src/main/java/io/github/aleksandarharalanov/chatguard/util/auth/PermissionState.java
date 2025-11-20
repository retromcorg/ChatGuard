package io.github.aleksandarharalanov.chatguard.util.auth;

public class PermissionState {
    public static final PermissionState TRUE = new PermissionState(true);
    public static final PermissionState FALSE = new PermissionState(false);
    
    private final boolean result;
    private final boolean hidden;

    private PermissionState(boolean result) {
        this(result, false);
    }

    public PermissionState(boolean result, boolean hidden) {
        this.result = result;
        this.hidden = hidden;
    }

    public boolean result() {
        return result;
    }

    public boolean hidden() {
        return hidden;
    }
}
