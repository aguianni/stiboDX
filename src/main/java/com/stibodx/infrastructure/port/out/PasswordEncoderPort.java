package com.stibodx.infrastructure.port.out;

public interface PasswordEncoderPort {
    String hash(String plainPassword);
    boolean check(String plainPassword, String hashedPassword);
}
