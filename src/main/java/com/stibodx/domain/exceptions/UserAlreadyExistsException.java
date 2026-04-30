package com.stibodx.domain.exceptions;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String field) {
        super("User with email: " + field + " already exists");
    }
}
