package com.example.verify_backend.Enums;

public enum ClientRole {
    CUSTOMER,
    CONTRACTOR,
    ;

    public static ClientRole fromStringSafe(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return ClientRole.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
