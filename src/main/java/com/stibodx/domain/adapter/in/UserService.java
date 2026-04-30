package com.stibodx.domain.adapter.in;

import com.stibodx.domain.entity.User;
import com.stibodx.domain.exceptions.InvalidEmailException;
import com.stibodx.domain.exceptions.UserAlreadyExistsException;
import com.stibodx.domain.port.out.UserRepositoryPort;
import com.stibodx.domain.port.in.UserServicePort;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Domain service for Users
 * Contains pure business logic independent of the framework
 */
@ApplicationScoped
public class UserService implements UserServicePort {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * Create a new user with duplicate validation
     */
    public User createUser(String email, String firstName, String lastName, String password) {
        logger.info("Creating user with email: {}", email);

        // Business validation
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email cannot be blank");
        }

        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            logger.warn("Invalid email format: {}", email);
            throw new InvalidEmailException(email);
        }

        if (StringUtils.isBlank(firstName)) {
            throw new IllegalArgumentException("First firstName cannot be blank");
        }

        if (StringUtils.isBlank(lastName)) {
            throw new IllegalArgumentException("Last firstName cannot be blank");
        }

        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("Password cannot be blank");
        }

        userRepositoryPort.findByEmail(email).ifPresent(u -> {
            logger.warn("User already exists with email: {}", email);
            throw new UserAlreadyExistsException(email);
        });

        // Create and save
        User nuevoUsuario = new User(email, firstName, lastName, password);
        User savedUser = userRepositoryPort.save(nuevoUsuario);
        logger.info("User created successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    /**
     * Get user by ID
     */
    public Optional<User> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be valid");
        }
        logger.debug("Finding user with id: {}", id);
        return userRepositoryPort.findById(id);
    }

    /**
     * Get user by email
     */
    public Optional<User> findByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email cannot be blank");
        }
        logger.debug("Finding user by email: {}", email);
        return userRepositoryPort.findByEmail(email);
    }

    /**
     * List all users
     */
    public List<User> findAll() {
        logger.debug("Listing all users");
        return userRepositoryPort.findAll();
    }

    /**
     * Update user
     */
    public User updateUser(Long id, String firstName, String lastName) {
        logger.info("Updating user with id: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be valid");
        }

        if (StringUtils.isBlank(firstName)) {
            throw new IllegalArgumentException("First firstName cannot be blank");
        }

        if (StringUtils.isBlank(lastName)) {
            throw new IllegalArgumentException("Last firstName cannot be blank");
        }

        User user = findById(id).orElseThrow();
        user.updateProfile(firstName, lastName);
        return userRepositoryPort.update(user);
    }

    /**
     * Delete user
     */
    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be valid");
        }
        userRepositoryPort.delete(id);
    }
}
