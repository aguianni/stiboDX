package com.stibodx.application.port.in;

import com.stibodx.domain.entity.User;

/**
 * Use case: create user
 */

public interface CreateUserPort {

    User execute(String email, String nombre, String apellido, String password);
}

