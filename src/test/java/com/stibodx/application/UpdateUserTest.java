package com.stibodx.application;

import com.stibodx.application.adapter.in.UpdateUser;
import com.stibodx.domain.entity.User;
import com.stibodx.domain.port.in.UserServicePort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para UpdateUserUseCase
 */
@QuarkusTest
@DisplayName("UpdateUserUseCase Tests")
class UpdateUserTest {

    @Inject
    UpdateUser updateUser;

    @InjectMock
    UserServicePort userServicePort;

    private User updatedMockUser;

    @BeforeEach
    void setUp() {
        // Usuario con datos actualizados
        updatedMockUser = new User(
            1L,
            "john.doe@example.com",
            "Jonathan",  // Nombre actualizado
            "Doe-Smith",  // Apellido actualizado
            "securePassword123",
            Instant.now(),
            Instant.now()
        );
    }

    @Test
    @DisplayName("Debe actualizar un usuario exitosamente")
    void testUpdateUserSuccessfully() {
        // Arrange
        when(userServicePort.updateUser(1L, "Jonathan", "Doe-Smith"))
            .thenReturn(updatedMockUser);

        // Act
        User result = updateUser.execute(1L, "Jonathan", "Doe-Smith");

        // Assert
        assertThat(result)
            .isNotNull()
            .satisfies(user -> {
                assertThat(user.getId()).isEqualTo(1L);
                assertThat(user.getFirstName()).isEqualTo("Jonathan");
                assertThat(user.getLastName()).isEqualTo("Doe-Smith");
                assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
            });

        // Verify
        verify(userServicePort).updateUser(1L, "Jonathan", "Doe-Smith");
    }

    @Test
    @DisplayName("Debe llamar al servicio con los parámetros correctos")
    void testUpdateUserCallsServiceWithCorrectParams() {
        // Arrange
        when(userServicePort.updateUser(42L, "NewName", "NewLastName"))
            .thenReturn(updatedMockUser);

        // Act
        updateUser.execute(42L, "NewName", "NewLastName");

        // Verify
        verify(userServicePort).updateUser(42L, "NewName", "NewLastName");
    }

    @Test
    @DisplayName("Debe actualizar solo el firstName del usuario")
    void testUpdateUserNameOnly() {
        // Arrange
        User userWithOnlyNameUpdated = new User(
            1L,
            "john.doe@example.com",
            "UpdatedName",
            "Doe",
            "securePassword123",
            Instant.now(),
            Instant.now()
        );

        when(userServicePort.updateUser(1L, "UpdatedName", "Doe"))
            .thenReturn(userWithOnlyNameUpdated);

        // Act
        User result = updateUser.execute(1L, "UpdatedName", "Doe");

        // Assert
        assertThat(result.getFirstName()).isEqualTo("UpdatedName");

        // Verify
        verify(userServicePort).updateUser(1L, "UpdatedName", "Doe");
    }

    @Test
    @DisplayName("Debe actualizar solo el lastName del usuario")
    void testUpdateUserLastNameOnly() {
        // Arrange
        User userWithOnlyLastNameUpdated = new User(
            1L,
            "john.doe@example.com",
            "John",
            "UpdatedLastName",
            "securePassword123",
            Instant.now(),
            Instant.now()
        );

        when(userServicePort.updateUser(1L, "John", "UpdatedLastName"))
            .thenReturn(userWithOnlyLastNameUpdated);

        // Act
        User result = updateUser.execute(1L, "John", "UpdatedLastName");

        // Assert
        assertThat(result.getLastName()).isEqualTo("UpdatedLastName");

        // Verify
        verify(userServicePort).updateUser(1L, "John", "UpdatedLastName");
    }

}

