package com.stibodx.infrastructure.adapter.out.persistence;

import com.stibodx.domain.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper to convert between User (domain) and UserJpaEntity (persistence)
 * Maintains the separation between layers
 */
@ApplicationScoped
public class UserMapper {

    /**
     * Convert from UserJpaEntity to User (domain)
     */
    public User toDomain(UserJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        return new User(
                jpaEntity.getId(),
                jpaEntity.getEmail(),
                jpaEntity.getFirstName(),
                jpaEntity.getLastName(),
                jpaEntity.getPassword(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt()
        );
    }

    /**
     * Convert from User (domain) to UserJpaEntity (persistence)
     */
    public UserJpaEntity toJpa(User domainUser) {
        if (domainUser == null) {
            return null;
        }
        UserJpaEntity jpaEntity = new UserJpaEntity(
                domainUser.getEmail(),
                domainUser.getFirstName(),
                domainUser.getLastName(),
                domainUser.getPassword(),
                domainUser.getCreatedAt(),
                domainUser.getUpdatedAt()
        );
        if (domainUser.getId() != null) {
            jpaEntity.setId(domainUser.getId());
        }
        return jpaEntity;
    }
}

