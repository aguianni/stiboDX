package com.stibodx.application.port.in;

import com.stibodx.domain.entity.User;

/**
 * Use case: find user by ID
 */
public interface FindUserByIdPort {

    User execute(Long id);
}

