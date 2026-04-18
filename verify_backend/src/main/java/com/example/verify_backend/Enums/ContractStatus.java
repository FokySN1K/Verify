package com.example.verify_backend.Enums;

public enum ContractStatus {
    // Только что созданный заказ
    NEW,
    TENDER,
    // Заказ находящийся в работе
    PROCESSING,
    // Завершенный заказ
    DONE,
    // Заказ отмененный
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
