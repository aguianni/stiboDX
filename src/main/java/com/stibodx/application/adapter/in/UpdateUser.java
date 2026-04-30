package com.stibodx.application.adapter.in;

import com.stibodx.application.port.in.UpdateUserPort;
import com.stibodx.domain.entity.User;
import com.stibodx.domain.port.in.UserServicePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Use case: Actualizar usuario
 */
@ApplicationScoped
public class UpdateUser implements UpdateUserPort {

    @Inject
    UserServicePort userService;

    @Transactional
    public User execute(Long id, String nombre, String apellido) {
        return userService.updateUser(id, nombre, apellido);
    }
}

