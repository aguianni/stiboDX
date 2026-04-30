package com.stibodx.infrastructure.adapter.out.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DTO para respuestas de error (Record de Java 21 - Immutable)
 */
public record ErrorResponse(
    String message,
    String timestamp
) {
    /**
     * Constructor auxiliar con valores por defecto
     */
    public ErrorResponse(String message) {
        this(message, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}

