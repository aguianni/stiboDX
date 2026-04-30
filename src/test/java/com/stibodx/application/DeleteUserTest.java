package com.stibodx.application;

import com.stibodx.application.adapter.in.DeleteUser;
import com.stibodx.domain.port.in.UserServicePort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * Tests unitarios para DeleteUserUseCase
 */
@QuarkusTest
@DisplayName("DeleteUserUseCase Tests")
class DeleteUserTest {

    @Inject
    DeleteUser deleteUser;

    @InjectMock
    UserServicePort userServicePort;

    @Test
    @DisplayName("Debe eliminar un usuario exitosamente")
    void testDeleteUserSuccessfully() {
        // Arrange
        doNothing().when(userServicePort).deleteUser(1L);

        // Act
        deleteUser.execute(1L);

        // Verify
        verify(userServicePort).deleteUser(1L);
    }

    @Test
    @DisplayName("Debe llamar al servicio con el ID correcto")
    void testDeleteUserCallsServiceWithCorrectId() {
        // Arrange
        doNothing().when(userServicePort).deleteUser(42L);

        // Act
        deleteUser.execute(42L);

        // Verify
        verify(userServicePort).deleteUser(42L);
    }

    @Test
    @DisplayName("Debe manejar eliminación de múltiples usuarios")
    void testDeleteMultipleUsers() {
        // Arrange
        doNothing().when(userServicePort).deleteUser(1L);
        doNothing().when(userServicePort).deleteUser(2L);
        doNothing().when(userServicePort).deleteUser(3L);

        // Act
        deleteUser.execute(1L);
        deleteUser.execute(2L);
        deleteUser.execute(3L);

        // Verify
        verify(userServicePort).deleteUser(1L);
        verify(userServicePort).deleteUser(2L);
        verify(userServicePort).deleteUser(3L);
    }

}

