package com.stibodx.infrastructure.adapter.out;

import com.stibodx.infrastructure.port.out.PasswordEncoderPort;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BCryptPasswordEncoder implements PasswordEncoderPort {
    @Override
    public String hash(String password) {
        return BcryptUtil.bcryptHash(password);
    }

    @Override
    public boolean check(String password, String hashedPassword) {
        return BcryptUtil.matches(password, hashedPassword);
    }
}
