package com.stibodx.application;

import com.stibodx.application.adapter.in.CreateUser;
import com.stibodx.domain.entity.User;
import com.stibodx.domain.port.in.UserServicePort;
import com.stibodx.infrastructure.port.out.PasswordEncoderPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para CreateUserUseCase
 */
@QuarkusTest
@DisplayName("CreateUserUseCase Tests")
class CreateUserTest {

    @Inject
    CreateUser createUser;

    @InjectMock
    UserServicePort userService;

    @InjectMock
    PasswordEncoderPort passwordEncoder;

    private User mockUser;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
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
    @DisplayName("Debe crear un usuario exitosamente")
    void testCreateUserSuccessfully() {
        // Arrange
        when(userService.createUser(
            "john.doe@example.com",
            "John",
            "Doe",
            "hashedPassword123"
        )).thenReturn(mockUser);

        when(passwordEncoder.hash("securePassword123")).thenReturn("hashedPassword123");

        // Act
        User result = createUser.execute(
            "john.doe@example.com",
            "John",
            "Doe",
            "securePassword123"
        );

        // Assert
        assertThat(result)
            .isNotNull()
            .satisfies(user -> {
                assertThat(user.getId()).isEqualTo(1L);
                assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
                assertThat(user.getFirstName()).isEqualTo("John");
                assertThat(user.getLastName()).isEqualTo("Doe");
            });

        // Verify
        verify(userService).createUser(
            "john.doe@example.com",
            "John",
            "Doe",
            "hashedPassword123"
        );
    }

    @Test
    @DisplayName("Debe llamar al servicio con los parámetros correctos")
    void testCreateUserCallsServiceWithCorrectParams() {
        // Arrange
        when(userService.createUser(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(mockUser);

        when(passwordEncoder.hash("password456")).thenReturn("hashedPassword");

        // Act
        createUser.execute(
            "jane.doe@example.com",
            "Jane",
            "Doe",
            "password456"
        );

        // Verify
        verify(userService).createUser(
            "jane.doe@example.com",
            "Jane",
            "Doe",
            "hashedPassword"
        );
    }

}

