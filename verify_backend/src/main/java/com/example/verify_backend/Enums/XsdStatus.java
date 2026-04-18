package com.example.verify_backend.Enums;

public enum XsdStatus {
    // Новый xsd документа находящийся на проверке у администратора
    NEW,
    // Старая версия xsd документа
    OLD,
    // Действующая версия xsd документа
    PROCESSING,
    ;

    public static XsdStatus fromStringSafe(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return XsdStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
