package com.nutritrack.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ROLE_PATIENT,
    ROLE_NUTRITIONIST,
    ROLE_ADMIN;

    @JsonCreator
    public static Role from(String value) {
        if (value == null) return null;
        String normalized = value.toUpperCase().trim();
        if (!normalized.startsWith("ROLE_")) {
            normalized = "ROLE_" + normalized;
        }
        return Role.valueOf(normalized);
    }
}