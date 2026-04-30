package com.stibodx.application;

import com.stibodx.application.adapter.in.ListUsers;
import com.stibodx.domain.entity.User;
import com.stibodx.domain.port.in.UserServicePort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para ListUsersUseCase
 */
@QuarkusTest
@DisplayName("ListUsersUseCase Tests")
class ListUsersTest {

    @Inject
    ListUsers listUsers;

    @InjectMock
    UserServicePort userServicePort;

    private List<User> mockUsers;

    @BeforeEach
    void setUp() {
        mockUsers = new ArrayList<>();
        mockUsers.add(new User(
            1L,
            "john.doe@example.com",
            "John",
            "Doe",
            "securePassword123",
            Instant.now(),
            Instant.now()
        ));
        mockUsers.add(new User(
            2L,
            "jane.smith@example.com",
            "Jane",
            "Smith",
            "securePassword456",
            Instant.now(),
            Instant.now()
        ));
    }

    @Test
    @DisplayName("Debe listar todos los usuarios exitosamente")
    void testListAllUsersSuccessfully() {
        // Arrange
        when(userServicePort.findAll()).thenReturn(mockUsers);

        // Act
        List<User> result = listUsers.execute();

        // Assert
        assertThat(result)
            .isNotNull()
            .hasSize(2)
            .contains(mockUsers.get(0), mockUsers.get(1));

        // Verify
        verify(userServicePort).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay usuarios")
    void testListAllUsersEmpty() {
        // Arrange
        when(userServicePort.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> result = listUsers.execute();

        // Assert
        assertThat(result).isEmpty();

        // Verify
        verify(userServicePort).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista con un usuario")
    void testListAllUsersWithSingleUser() {
        // Arrange
        List<User> singleUserList = new ArrayList<>();
        singleUserList.add(mockUsers.get(0));
        when(userServicePort.findAll()).thenReturn(singleUserList);

        // Act
        List<User> result = listUsers.execute();

        // Assert
        assertThat(result)
            .isNotNull()
            .hasSize(1)
            .contains(mockUsers.get(0));

        // Verify
        verify(userServicePort).findAll();
    }

    @Test
    @DisplayName("Debe llamar al servicio para obtener la lista de usuarios")
    void testListAllUsersCallsService() {
        // Arrange
        when(userServicePort.findAll()).thenReturn(mockUsers);

        // Act
        listUsers.execute();

        // Verify
        verify(userServicePort).findAll();
    }

}

