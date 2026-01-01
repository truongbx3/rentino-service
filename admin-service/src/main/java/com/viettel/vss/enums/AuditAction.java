package com.viettel.vss.enums;

public enum AuditAction {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    ASSIGN("assign");

    private final String action;

    AuditAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return action;
    }

    // Optional: convert String -> enum
    public static AuditAction fromString(String action) {
        for (AuditAction a : AuditAction.values()) {
            if (a.action.equalsIgnoreCase(action)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown action: " + action);
    }
}
