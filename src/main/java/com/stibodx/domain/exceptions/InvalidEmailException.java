package com.stibodx.domain.exceptions;

public class InvalidEmailException extends DomainException {

    public InvalidEmailException(String email) {
        super("Email: " + email + " must be invalid");
    }
}
