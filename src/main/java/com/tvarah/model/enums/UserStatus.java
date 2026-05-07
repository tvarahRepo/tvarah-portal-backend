package com.tvarah.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    PENDING("Pending"),
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String dbValue;

    public static UserStatus fromDbValue(String value) {
        for (UserStatus s : values()) {
            if (s.dbValue.equalsIgnoreCase(value)) return s;
        }
        throw new IllegalArgumentException("Unknown user status: " + value);
    }
}
