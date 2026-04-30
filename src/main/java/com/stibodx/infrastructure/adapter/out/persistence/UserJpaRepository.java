package com.stibodx.infrastructure.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * Quarkus Panache Repository para UserJpaEntity
 * Proporciona operaciones básicas de CRUD
 */
@ApplicationScoped
public class UserJpaRepository implements PanacheRepository<UserJpaEntity> {

    public Optional<UserJpaEntity> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public UserJpaEntity merge(UserJpaEntity entity) {
        return getEntityManager().merge(entity);
    }
}

