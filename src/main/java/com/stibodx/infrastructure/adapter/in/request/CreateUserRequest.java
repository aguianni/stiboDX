package com.stibodx.infrastructure.adapter.in.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear un usuario (Record de Java 21 - Immutable)
 */
public record CreateUserRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "First firstName cannot be blank")
        String firstName,

        @NotBlank(message = "Last firstName cannot be blank")
        String lastName,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password should be valid")
        String password
) {}

