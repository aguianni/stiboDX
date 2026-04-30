package com.stibodx.infrastructure.adapter.out.response;

import java.time.Instant;

/**
 * DTO for responding with user data (Java 21 Record - Immutable)
 */
public record UserResponse(
    Long id,
    String email,
    String firstName,
    String lastName,
    Instant createdAt,
    Instant updatedAt
) {}

