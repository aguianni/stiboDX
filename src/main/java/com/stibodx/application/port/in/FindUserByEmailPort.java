package com.stibodx.application.port.in;

import com.stibodx.domain.entity.User;

/**
 * Use case: find user by email
 */
public interface FindUserByEmailPort {

    User execute(String email);
}

