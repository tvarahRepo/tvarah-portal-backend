package com.tvarah.model.enums;

public enum UserStatus {
    DRAFT("Draft"),
    PENDING("Pending"),
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String dbValue;

    UserStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() { return dbValue; }

    public static UserStatus fromDbValue(String value) {
        for (UserStatus s : values()) {
            if (s.dbValue.equalsIgnoreCase(value)) return s;
        }
        throw new IllegalArgumentException("Unknown user status: " + value);
    }
}
