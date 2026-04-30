package com.stibodx.infrastructure.adapter.in.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para actualizar un usuario (Record de Java 21 - Immutable)
 */
public record UpdateUserRequest(
        @NotBlank(message = "Name can not be blank")
        String firstName,

        @NotBlank(message = "Lastname can not be blank")
        String lastName
) {}

