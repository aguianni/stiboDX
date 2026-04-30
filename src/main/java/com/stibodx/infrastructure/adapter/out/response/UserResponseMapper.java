package com.stibodx.infrastructure.adapter.out.response;

import com.stibodx.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper to convert User (domain) to UserResponse (DTO)
 */
@ApplicationScoped
public class UserResponseMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

