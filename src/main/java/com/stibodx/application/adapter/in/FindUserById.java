package com.stibodx.application.adapter.in;

import com.stibodx.application.port.in.FindUserByIdPort;
import com.stibodx.domain.entity.User;
import com.stibodx.domain.port.in.UserServicePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

/**
 * Use case: Obtener usuario por ID
 */
@ApplicationScoped
public class FindUserById implements FindUserByIdPort {

    @Inject
    UserServicePort userService;

    public User execute(Long id) {
        return userService.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));
    }
}

