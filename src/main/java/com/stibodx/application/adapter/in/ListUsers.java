package com.stibodx.application.adapter.in;

import com.stibodx.application.port.in.ListUsersPort;
import com.stibodx.domain.entity.User;
import com.stibodx.domain.port.in.UserServicePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Use case: Listar todos los usuarios
 */
@ApplicationScoped
public class ListUsers implements ListUsersPort {

    @Inject
    UserServicePort userService;

    public List<User> execute() {
        return userService.findAll();
    }
}

