package com.tvarah.model.enums;

public enum UserRole {

    // ── Internal Roles ────────────────────────────────────────────────────────
    ADMIN("Admin", RoleType.INTERNAL),
    MANAGEMENT("Management", RoleType.INTERNAL),
    ACCOUNT_MANAGER("Account Manager", RoleType.INTERNAL),
    RECRUITER("Recruiter", RoleType.INTERNAL),
    PANEL("Panel", RoleType.INTERNAL),
    SALES_AND_BD("Sales and BD", RoleType.INTERNAL),
    CANDIDATE("Candidate", RoleType.INTERNAL),

    // ── External / Client Roles ───────────────────────────────────────────────
    CLIENT_TA_HEAD("Client-TA Head", RoleType.EXTERNAL),
    CLIENT_TA_ASSOCIATE("Client-TA Associate", RoleType.EXTERNAL),
    CLIENT_HIRING_MANAGER("Client-Hiring Manager", RoleType.EXTERNAL),
    CLIENT_DIRECTOR("Client-Director", RoleType.EXTERNAL);

    private final String displayName;
    private final RoleType type;

    UserRole(String displayName, RoleType type) {
        this.displayName = displayName;
        this.type = type;
    }

    public String getDisplayName() { return displayName; }
    public RoleType getType() { return type; }

    public enum RoleType {
        INTERNAL, EXTERNAL
    }

    public static UserRole fromDisplayName(String displayName) {
        for (UserRole r : values()) {
            if (r.displayName.equalsIgnoreCase(displayName)) return r;
        }
        throw new IllegalArgumentException("Unknown role: " + displayName);
    }
}
