package com.stibodx.domain.exceptions;

/**
 * Excepción de negocio personalizada para errores controlados
 */
public abstract class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

