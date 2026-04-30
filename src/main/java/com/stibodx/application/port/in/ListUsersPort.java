package com.stibodx.application.port.in;

import com.stibodx.domain.entity.User;

import java.util.List;

/**
 * Use case: Listar todos los usuarios
 */
public interface ListUsersPort {

    List<User> execute();
}

