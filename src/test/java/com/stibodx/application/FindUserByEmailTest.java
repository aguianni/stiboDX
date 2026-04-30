package com.stibodx.application;

import com.stibodx.application.adapter.in.FindUserByEmail;
import com.stibodx.domain.entity.User;
import com.stibodx.domain.port.in.UserServicePort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para GetUserByEmailUseCase
 */
@QuarkusTest
@DisplayName("GetUserByEmailUseCase Tests")
class FindUserByEmailTest {

    @Inject
    FindUserByEmail findUserByEmail;

    @InjectMock
    UserServicePort userServicePort;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User(
            1L,
            "john.doe@example.com",
            "John",
            "Doe",
            "securePassword123",
            Instant.now(),
            Instant.now()
        );
    }

    @Test
    @DisplayName("Debe obtener un usuario existente por email")
    void testGetUserByEmailSuccessfully() {
        // Arrange
        when(userServicePort.findByEmail("john.doe@example.com"))
            .thenReturn(Optional.of(mockUser));

        // Act
        User result = findUserByEmail.execute("john.doe@example.com");

        // Assert
        assertThat(result)
            .satisfies(user -> {
                assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
                assertThat(user.getFirstName()).isEqualTo("John");
            });

        // Verify
        verify(userServicePort).findByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando el email no existe")
    void testGetUserByEmailNotFound() {
        // Arrange
        when(userServicePort.findByEmail("nonexistent@example.com"))
            .thenReturn(Optional.empty());

        // Act
        assertThatThrownBy(() -> findUserByEmail.execute("nonexistent@example.com"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with email: nonexistent@example.com does not exist");

        // Verify
        verify(userServicePort).findByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Debe llamar al servicio con el email correcto")
    void testGetUserByEmailCallsServiceWithCorrectEmail() {
        // Arrange
        when(userServicePort.findByEmail("test@example.com"))
            .thenReturn(Optional.of(mockUser));

        // Act
        findUserByEmail.execute("test@example.com");

        // Verify
        verify(userServicePort).findByEmail("test@example.com");
    }

}


