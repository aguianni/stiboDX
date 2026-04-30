package com.stibodx.infrastructure.adapter.out.persistence;

import com.stibodx.domain.entity.User;
import com.stibodx.domain.port.out.UserRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia que implementa el puerto UserRepositoryPort
 * Implementa la comunicación con la base de datos MySQL
 */
@ApplicationScoped
@Transactional
public class UserRepositoryAdapter implements UserRepositoryPort {

    @Inject
    UserJpaRepository userJpaRepository;

    @Inject
    UserMapper userMapper;

    @Override
    public User save(User user) {
        UserJpaEntity jpaEntity = userMapper.toJpa(user);
        userJpaRepository.persist(jpaEntity);
        return userMapper.toDomain(jpaEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserJpaEntity> jpaEntity = userJpaRepository.findByIdOptional(id);
        return jpaEntity.map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserJpaEntity> jpaEntity = userJpaRepository.findByEmail(email);
        return jpaEntity.map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User update(User user) {
        UserJpaEntity jpaEntity = userMapper.toJpa(user);
        UserJpaEntity jpaEntityModified = userJpaRepository.merge(jpaEntity);
        return userMapper.toDomain(jpaEntityModified);
    }

    @Override
    public void delete(Long id) {
        userJpaRepository.deleteById(id);
    }
}

