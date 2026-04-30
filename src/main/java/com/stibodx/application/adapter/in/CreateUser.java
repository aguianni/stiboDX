package com.stibodx.application.adapter.in;

import com.stibodx.application.port.in.CreateUserPort;
import com.stibodx.domain.entity.User;
import com.stibodx.domain.port.in.UserServicePort;
import com.stibodx.infrastructure.adapter.out.BCryptPasswordEncoder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUser implements CreateUserPort {

    @Inject
    UserServicePort userService;

    @Inject
    BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User execute(String email, String nombre, String apellido, String password) {
        String hashed = passwordEncoder.hash(password);
        return userService.createUser(email, nombre, apellido, hashed);
    }
}

