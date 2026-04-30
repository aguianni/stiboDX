package com.stibodx.application.adapter.in;

import com.stibodx.application.port.in.DeleteUserPort;
import com.stibodx.domain.port.in.UserServicePort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Use case: Eliminar usuario
 */
@ApplicationScoped
public class DeleteUser implements DeleteUserPort {

    @Inject
    UserServicePort userService;

    @Transactional
    public void execute(Long id) {
        userService.deleteUser(id);
    }
}

