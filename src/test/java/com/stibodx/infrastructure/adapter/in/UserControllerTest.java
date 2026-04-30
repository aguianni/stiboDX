package com.stibodx.infrastructure.adapter.in;

import com.stibodx.application.adapter.in.*;
import com.stibodx.domain.entity.User;
import com.stibodx.infrastructure.adapter.in.request.CreateUserRequest;
import com.stibodx.infrastructure.adapter.in.request.UpdateUserRequest;
import com.stibodx.infrastructure.adapter.out.response.UserResponse;
import com.stibodx.infrastructure.adapter.out.response.UserResponseMapper;
import io.quarkus.hibernate.validator.runtime.jaxrs.ResteasyReactiveViolationException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para UserController
 * Prueba los endpoints REST de la API de usuarios
 */
@QuarkusTest
@DisplayName("UserController Tests")
class UserControllerTest {

    @Inject
    UserController userController;

    @InjectMock
    CreateUser createUserUseCase;

    @InjectMock
    FindUserById findUserByIdUseCase;

    @InjectMock
    FindUserByEmail findUserByEmailUseCase;

    @InjectMock
    ListUsers listUsersUseCase;

    @InjectMock
    UpdateUser updateUserUseCase;

    @InjectMock
    DeleteUser deleteUserUseCase;

    @InjectMock
    UserResponseMapper userResponseMapper;

    private User mockUser;
    private User mockUser2;
    private UserResponse mockUserResponse;
    private UserResponse mockUserResponse2;
    private Instant now;

    @BeforeEach
    void setUp() {
        now = Instant.now();

        // Test data for user 1
        mockUser = new User(
                1L,
                "john.doe@example.com",
                "John",
                "Doe",
                "securePassword123",
                now,
                now
        );

        // Test data for user 2
        mockUser2 = new User(
                2L,
                "jane.smith@example.com",
                "Jane",
                "Smith",
                "password456",
                now,
                now
        );

        // Expected response
        mockUserResponse = new UserResponse(
                1L,
                "john.doe@example.com",
                "John",
                "Doe",
                now,
                now
        );

        // Expected response
        mockUserResponse2 = new UserResponse(
                2L,
                "jane.smith@example.com",
                "Jane",
                "Smith",
                now,
                now
        );
    }

    // ================== POST - CREATE USER ==================
    @Test
    @DisplayName("Should create a user with valid data")
    void testCreateUserSuccessfully() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "john.doe@example.com",
                "John",
                "Doe",
                "securePassword123"
        );
        when(createUserUseCase.execute(
                "john.doe@example.com",
                "John",
                "Doe",
                "securePassword123"
        )).thenReturn(mockUser);
        when(userResponseMapper.toResponse(mockUser)).thenReturn(mockUserResponse);

        // Act
        Response response = userController.create(request);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(mockUserResponse);

        // Verify
        verify(createUserUseCase, times(1)).execute(
                "john.doe@example.com",
                "John",
                "Doe",
                "securePassword123"
        );
        verify(userResponseMapper).toResponse(mockUser);
    }

    @Test
    @DisplayName("Should throw ResteasyReactiveViolationException when email is blank")
    void testCreateThrowsResteasyReactiveViolationExceptionWhenEmailIsBlank() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "",
                "Test",
                "User",
                "password123"
        );
        when(createUserUseCase.execute(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockUser);
        when(userResponseMapper.toResponse(any())).thenReturn(mockUserResponse);

        // Act

        assertThatThrownBy(() -> userController.create(request))
                .isInstanceOf(ResteasyReactiveViolationException.class)
                .hasMessageContaining("create.request.email: Email cannot be blank");

        // Verify
        verify(createUserUseCase, never()).execute(
                "",
                "Test",
                "User",
                "password123"
        );
    }

    // ================== GET - FIND BY ID ==================
    @Test
    @DisplayName("Should retrieve a user by email successfully")
    void testFindByIdSuccessfully() {
        // Arrange
        when(findUserByIdUseCase.execute(1L)).thenReturn(mockUser);
        when(userResponseMapper.toResponse(mockUser)).thenReturn(mockUserResponse);

        // Act
        Response response = userController.findById(1L);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(mockUserResponse);

        // Verify
        verify(findUserByIdUseCase).execute(1L);
        verify(userResponseMapper).toResponse(mockUser);
    }

    @Test
    @DisplayName("Should return 404 when user by email does not exist")
    void testFindByIdThrowsNotFoundExceptionWhenUserDoesNotExist() {
        // Arrange
        Long userId = 999L;
        when(findUserByIdUseCase.execute(userId)).thenThrow(new NotFoundException("User does not exist"));

        // Act & Assert
        assertThatThrownBy(() -> userController.findUserById.execute(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User does not exist");

        // Verify
        verify(findUserByIdUseCase).execute(userId);
    }

    // ================== GET - FIND BY EMAIL ==================

    @Test
    @DisplayName("Should retrieve a user by email successfully")
    void testFindByEmailSuccessfully() {
        // Arrange
        String email = "john.doe@example.com";
        when(findUserByEmailUseCase.execute(email)).thenReturn(mockUser);
        when(userResponseMapper.toResponse(mockUser)).thenReturn(mockUserResponse);

        // Act
        Response response = userController.findByEmail(email);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(mockUserResponse);

        // Verify
        verify(findUserByEmailUseCase).execute(email);
        verify(userResponseMapper).toResponse(mockUser);
    }

    @Test
    @DisplayName("Should return 404 when user by email does not exist")
    void testFindByEmailReturnsNotFoundWhenUserDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";
        when(findUserByEmailUseCase.execute(email)).thenThrow(new NotFoundException("User with email: " + email + " does not exist"));

        // Act & Assert
        assertThatThrownBy(() -> userController.findUserByEmail.execute(email))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with email: nonexistent@example.com does not exist");

        verify(findUserByEmailUseCase).execute(email);
    }

    // ================== GET - LIST ALL ==================

    @Test
    @DisplayName("Should list all users successfully")
    void testFindAllSuccessfully() {
        // Arrange
        UserResponse userResponse2 = new UserResponse(
                2L,
                "jane.smith@example.com",
                "Jane",
                "Smith",
                now,
                now
        );
        List<User> users = List.of(mockUser, mockUser2);
        List<UserResponse> userResponses = List.of(mockUserResponse, userResponse2);

        when(listUsersUseCase.execute()).thenReturn(users);
        when(userResponseMapper.toResponse(mockUser)).thenReturn(mockUserResponse);
        when(userResponseMapper.toResponse(mockUser2)).thenReturn(userResponse2);

        // Act
        Response response = userController.findAll();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(userResponses);

        // Verify
        verify(listUsersUseCase).execute();
        verify(userResponseMapper, times(2)).toResponse(any());
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void testFindAllReturnsEmptyListWhenNoUsers() {
        // Arrange
        when(listUsersUseCase.execute()).thenReturn(List.of());

        // Act
        Response response = userController.findAll();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat((List<?>) response.getEntity()).isEmpty();

        verify(listUsersUseCase).execute();
    }

    @Test
    @DisplayName("Should correctly map all users in the list")
    void testFindAllMapsAllUsersCorrectly() {
        // Arrange
        List<User> users = List.of(mockUser, mockUser2);
        when(listUsersUseCase.execute()).thenReturn(users);
        when(userResponseMapper.toResponse(mockUser)).thenReturn(mockUserResponse);
        when(userResponseMapper.toResponse(mockUser2)).thenReturn(mockUserResponse2);

        // Act
        userController.findAll();

        // Verify
        verify(userResponseMapper).toResponse(mockUser);
        verify(userResponseMapper).toResponse(mockUser2);
    }

    // ================== PUT - UPDATE USER ==================

    @Test
    @DisplayName("Should update a user successfully")
    void testUpdateUserSuccessfully() {
        // Arrange
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("John Updated", "Doe Updated");
        User updatedUser = new User(
                userId,
                "john.doe@example.com",
                "John Updated",
                "Doe Updated",
                "securePassword123",
                now,
                now
        );
        UserResponse updatedResponse = new UserResponse(
                userId,
                "john.doe@example.com",
                "John Updated",
                "Doe Updated",
                now,
                now
        );

        when(updateUserUseCase.execute(userId, "John Updated", "Doe Updated"))
                .thenReturn(updatedUser);
        when(userResponseMapper.toResponse(updatedUser)).thenReturn(updatedResponse);

        // Act
        Response response = userController.update(userId, request);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(updatedResponse);

        // Verify
        verify(updateUserUseCase).execute(userId, "John Updated", "Doe Updated");
        verify(userResponseMapper).toResponse(updatedUser);
    }

    @Test
    @DisplayName("Should call updateUserUseCase with correct parameters")
    void testUpdateUserCallsUseCaseWithCorrectParams() {
        // Arrange
        Long userId = 1L;
        UpdateUserRequest request = new UpdateUserRequest("Jane", "Smith");
        when(updateUserUseCase.execute(userId, "Jane", "Smith")).thenReturn(mockUser);
        when(userResponseMapper.toResponse(any())).thenReturn(mockUserResponse);

        // Act
        userController.update(userId, request);

        // Verify
        verify(updateUserUseCase, times(1)).execute(userId, "Jane", "Smith");
    }

    // ================== DELETE - DELETE USER ==================

    @Test
    @DisplayName("Should delete a user successfully")
    void testDeleteUserSuccessfully() {
        // Arrange
        Long userId = 1L;
        doNothing().when(deleteUserUseCase).execute(userId);

        // Act
        Response response = userController.delete(userId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());

        // Verify
        verify(deleteUserUseCase, times(1)).execute(userId);
    }

    @Test
    @DisplayName("Debe llamar a deleteUserUseCase una sola vez")
    void testDeleteUserCallsUseCaseOnce() {
        // Arrange
        Long userId = 5L;
        doNothing().when(deleteUserUseCase).execute(userId);

        // Act
        Response response = userController.delete(userId);

        // Verify
        verify(deleteUserUseCase, times(1)).execute(userId);
        // Assert
        assertThat(response.getStatus()).isEqualTo(204); // NO_CONTENT
        assertThat(response.hasEntity()).isFalse();
    }



    // ================== INTEGRACIÓN - MULTIPLE OPERATIONS ==================

    @Test
    @DisplayName("Should allow creating and then retrieving a user")
    void testCreateAndRetrieveUserFlow() {
        // Arrange
        CreateUserRequest createRequest = new CreateUserRequest(
                "john.doe@example.com",
                "John",
                "Doe",
                "securePassword123"
        );
        when(createUserUseCase.execute(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mockUser);
        when(userResponseMapper.toResponse(mockUser)).thenReturn(mockUserResponse);
        when(findUserByIdUseCase.execute(1L)).thenReturn(mockUser);

        // Act
        Response createResponse = userController.create(createRequest);
        Response getResponse = userController.findById(1L);

        // Assert
        assertThat(createResponse.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(getResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(createResponse.getEntity()).isEqualTo(getResponse.getEntity());

        // Verify
        verify(createUserUseCase).execute(
                "john.doe@example.com",
                "John",
                "Doe",
                "securePassword123"
        );
        verify(findUserByIdUseCase).execute(1L);
    }

}