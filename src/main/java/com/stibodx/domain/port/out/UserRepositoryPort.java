package com.stibodx.domain.port.out;

import com.stibodx.domain.entity.User;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de usuarios
 * Define el contrato que debe cumplir cualquier implementación de persistencia
 */
public interface UserRepositoryPort {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    User update(User user);

    void delete(Long id);
}

