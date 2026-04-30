package com.stibodx.application;

import com.stibodx.application.adapter.in.FindUserById;
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
 * Tests unitarios para GetUserUseCase
 */
@QuarkusTest
@DisplayName("GetUserUseCase Tests")
class FindUserByIdTest {

    @Inject
    FindUserById findUserById;

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
    @DisplayName("Debe obtener un usuario existente por ID")
    void testGetUserByIdSuccessfully() {
        // Arrange
        when(userServicePort.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        User result = findUserById.execute(1L);

        // Assert
        assertThat(result).isEqualTo(mockUser);

        // Verify
        verify(userServicePort).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando el usuario no existe")
    void testGetUserByIdNotFound() {
        // Arrange
        when(userServicePort.findById(999L)).thenReturn(Optional.empty());

        // Act
        assertThatThrownBy(() -> findUserById.execute(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User does not exist");

        // Verify
        verify(userServicePort).findById(999L);
    }

    @Test
    @DisplayName("Debe llamar al servicio con el ID correcto")
    void testGetUserCallsServiceWithCorrectId() {
        // Arrange
        when(userServicePort.findById(42L)).thenReturn(Optional.of(mockUser));

        // Act
        findUserById.execute(42L);

        // Verify
        verify(userServicePort).findById(42L);
    }

}