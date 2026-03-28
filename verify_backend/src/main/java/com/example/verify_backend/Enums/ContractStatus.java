package com.example.verify_backend.Enums;

public enum ContractStatus {
    NEW,
    TENDER,
    PROCESSING,
    DONE,
    FAILED,
    ;

    public static ContractStatus fromStringSafe(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return ContractStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
