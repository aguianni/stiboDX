package com.stibodx.application.port.in;

import com.stibodx.domain.entity.User;

/**
 * Use case: update user
 */
public interface UpdateUserPort {

    User execute(Long id, String nombre, String apellido);

}

