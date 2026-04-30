package com.stibodx.domain.service;

import com.stibodx.domain.entity.User;
import com.stibodx.domain.exceptions.InvalidEmailException;
import com.stibodx.domain.exceptions.UserAlreadyExistsException;
import com.stibodx.domain.port.out.UserRepositoryPort;
import com.stibodx.domain.adapter.in.UserService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
@DisplayName("UserService - Tests de Lógica de Negocio")
class UserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @InjectMock
    UserRepositoryPort userRepositoryPort;

    @Inject
    UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("john@example.com", "John", "Doe", "password123");
        mockUser.setId(1L);
    }

    // ==================== CREATE USER TESTS ====================

    @Test
    @DisplayName("Create user successfully")
    void testCreateUserSuccessfully() {
        when(userRepositoryPort.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(userRepositoryPort.save(any(User.class))).thenReturn(mockUser);

        User result = userService.createUser("john@example.com", "John", "Doe", "password123");

        assertThat(result)
                .isNotNull()
                .satisfies(user -> {
                    assertThat(user.getEmail()).isEqualTo("john@example.com");
                    assertThat(user.getFirstName()).isEqualTo("John");
                    assertThat(user.getLastName()).isEqualTo("Doe");
                });

        verify(userRepositoryPort).findByEmail("john@example.com");
        verify(userRepositoryPort).save(any(User.class));
    }

    @Test
    @DisplayName("Create user with empty email")
    void testCreateUserWithEmptyEmail() {
        assertThatThrownBy(() -> userService.createUser("", "John", "Doe", "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email cannot be blank");

        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Fallar cuando el email es null")
    void testCreateUserWithNullEmail() {
        assertThatThrownBy(() -> userService.createUser(null, "John", "Doe", "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email cannot be blank");

        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Fallar cuando el email no es válido")
    void testCreateUserWithInvalidEmail() {
        assertThatThrownBy(() -> userService.createUser("invalidemail", "John", "Doe", "password123"))
                .isInstanceOf(InvalidEmailException.class)
                .hasMessageContaining("Email: invalidemail must be invalid");

        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Fallar cuando el firstName está vacío")
    void testCreateUserWithEmptyNombre() {
        when(userRepositoryPort.findByEmail("john@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.createUser("john@example.com", "", "Doe", "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First firstName cannot be blank");

        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Fallar cuando el lastName está vacío")
    void testCreateUserWithEmptyApellido() {
        when(userRepositoryPort.findByEmail("john@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.createUser("john@example.com", "John", "", "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last firstName cannot be blank");

        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Fallar cuando la contraseña está vacía")
    void testCreateUserWithEmptyPassword() {
        when(userRepositoryPort.findByEmail("john@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.createUser("john@example.com", "John", "Doe", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password cannot be blank");

        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Fallar cuando el email ya existe")
    void testCreateUserWithDuplicateEmail() {
        when(userRepositoryPort.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));

        assertThatThrownBy(() -> userService.createUser("john@example.com", "John", "Doe", "password123"))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User with email: john@example.com already exists");

        verify(userRepositoryPort, never()).save(any());
    }

    // ==================== FIND BY ID TESTS ====================

    @Test
    @DisplayName("Obtener usuario por ID exitosamente")
    void testFindByIdSuccessfully() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.findById(1L);

        assertThat(result)
                .isPresent()
                .contains(mockUser);

        verify(userRepositoryPort).findById(1L);
    }

    @Test
    @DisplayName("Retornar empty cuando el usuario no existe")
    void testFindByIdNotFound() {
        when(userRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(999L);

        assertThat(result).isEmpty();
        verify(userRepositoryPort).findById(999L);
    }

    @Test
    @DisplayName("Invalid ID")
    void testFindByIdWithInvalidId() {
        assertThatThrownBy(() -> userService.findById(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id must be valid");

        assertThatThrownBy(() -> userService.findById(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id must be valid");

        assertThatThrownBy(() -> userService.findById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id must be valid");

        verify(userRepositoryPort, never()).findById(any());
    }

    // ==================== FIND BY EMAIL TESTS ====================

    @Test
    @DisplayName("Obtener usuario por email exitosamente")
    void testFindByEmailSuccessfully() {
        when(userRepositoryPort.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.findByEmail("john@example.com");

        assertThat(result)
                .isPresent()
                .contains(mockUser);

        verify(userRepositoryPort).findByEmail("john@example.com");
    }

    @Test
    @DisplayName("Retornar empty cuando el email no existe")
    void testFindByEmailNotFound() {
        when(userRepositoryPort.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail("notfound@example.com");

        assertThat(result).isEmpty();
        verify(userRepositoryPort).findByEmail("notfound@example.com");
    }

    @Test
    @DisplayName("Retornar empty cuando el email es null")
    void testFindByEmailWithNullEmail() {

        assertThatThrownBy(() -> userService.findByEmail(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email cannot be blank");

        verify(userRepositoryPort, never()).findByEmail(any());
    }

    @Test
    @DisplayName("Retornar empty cuando el email está vacío")
    void testFindByEmailWithEmptyEmail() {
        assertThatThrownBy(() -> userService.findByEmail(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email cannot be blank");

        verify(userRepositoryPort, never()).findByEmail(any());
    }

    // ==================== FIND ALL TESTS ====================

    @Test
    @DisplayName("Listar todos los usuarios exitosamente")
    void testFindAllSuccessfully() {
        User user2 = new User("jane@example.com", "Jane", "Smith", "password123");
        user2.setId(2L);
        List<User> mockUsers = Arrays.asList(mockUser, user2);

        when(userRepositoryPort.findAll()).thenReturn(mockUsers);

        List<User> result = userService.findAll();

        assertThat(result)
                .isNotEmpty()
                .hasSize(2)
                .contains(mockUser, user2);

        verify(userRepositoryPort).findAll();
    }

    @Test
    @DisplayName("Retornar lista vacía cuando no hay usuarios")
    void testFindAllEmpty() {
        when(userRepositoryPort.findAll()).thenReturn(Arrays.asList());

        List<User> result = userService.findAll();

        assertThat(result).isEmpty();
        verify(userRepositoryPort).findAll();
    }

    // ==================== UPDATE USER TESTS ====================

    @Test
    @DisplayName("Actualizar usuario exitosamente")
    void testUpdateUserSuccessfully() {
        User updatedUser = new User(1L, "john@example.com", "Jonathan", "Doeson", "password123",
                mockUser.getCreatedAt(), mockUser.getUpdatedAt());

        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepositoryPort.update(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, "Jonathan", "Doeson");

        assertThat(result)
                .isNotNull()
                .satisfies(user -> {
                    assertThat(user.getFirstName()).isEqualTo("Jonathan");
                    assertThat(user.getLastName()).isEqualTo("Doeson");
                });

        verify(userRepositoryPort).findById(1L);
        verify(userRepositoryPort).update(any(User.class));
    }

    @Test
    @DisplayName("Fallar al actualizar cuando el ID es inválido")
    void testUpdateUserWithInvalidId() {
        assertThatThrownBy(() -> userService.updateUser(0L, "John", "Doe"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id must be valid");

        verify(userRepositoryPort, never()).update(any());
    }

    @Test
    @DisplayName("Fallar al actualizar cuando el firstName está vacío")
    void testUpdateUserWithEmptyNombre() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(mockUser));

        assertThatThrownBy(() -> userService.updateUser(1L, "", "Doe"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First firstName cannot be blank");

        verify(userRepositoryPort, never()).update(any());
    }

    @Test
    @DisplayName("Fallar al actualizar cuando el lastName está vacío")
    void testUpdateUserWithEmptyApellido() {
        when(userRepositoryPort.findById(1L)).thenReturn(Optional.of(mockUser));

        assertThatThrownBy(() -> userService.updateUser(1L, "John", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last firstName cannot be blank");

        verify(userRepositoryPort, never()).update(any());
    }

    @Test
    @DisplayName("Fallar al actualizar cuando el usuario no existe")
    void testUpdateUserNotFound() {
        when(userRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(999L, "John", "Doe"))
                .isInstanceOf(java.util.NoSuchElementException.class);

        verify(userRepositoryPort, never()).update(any());
    }

    // ==================== DELETE USER TESTS ====================

    @Test
    @DisplayName("Eliminar usuario exitosamente")
    void testDeleteUserSuccessfully() {
        userService.deleteUser(1L);
        verify(userRepositoryPort).delete(1L);
    }

    @Test
    @DisplayName("Fallar al eliminar cuando el ID es inválido")
    void testDeleteUserWithInvalidId() {
        assertThatThrownBy(() -> userService.deleteUser(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Id must be valid");

        verify(userRepositoryPort, never()).delete(any());
    }
}
