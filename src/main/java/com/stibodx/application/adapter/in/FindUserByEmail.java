package com.stibodx.application.adapter.in;

import com.stibodx.application.port.in.FindUserByEmailPort;
import com.stibodx.domain.entity.User;
import com.stibodx.domain.port.in.UserServicePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

/**
 * Use case: Obtener usuario por email
 */
@ApplicationScoped
public class FindUserByEmail implements FindUserByEmailPort {

    @Inject
    UserServicePort userService;

    public User execute(String email) {
        return userService.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email: " + email + " does not exist"));
    }
}

