package com.stibodx.domain.port.in;

import com.stibodx.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserServicePort {

    User createUser(String email, String nombre, String apellido, String password);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User updateUser(Long id, String nombre, String apellido);
    void deleteUser(Long id);
}
