package com.stibodx.infrastructure.adapter.in;

import com.stibodx.application.adapter.in.*;
import com.stibodx.domain.entity.User;
import com.stibodx.infrastructure.adapter.in.request.CreateUserRequest;
import com.stibodx.infrastructure.adapter.in.request.UpdateUserRequest;
import com.stibodx.infrastructure.adapter.out.response.UserResponse;
import com.stibodx.infrastructure.adapter.out.response.UserResponseMapper;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Adapter (Controller) - HTTP entry point
 * Exposes the REST API endpoints
 * Exceptions are handled automatically by ExceptionMappers
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User Management", description = "API for managing users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Inject
    CreateUser createUser;

    @Inject
    FindUserById findUserById;

    @Inject
    FindUserByEmail findUserByEmail;

    @Inject
    ListUsers listUsers;

    @Inject
    UpdateUser updateUser;

    @Inject
    DeleteUser deleteUser;

    @Inject
    UserResponseMapper userResponseMapper;

    @POST
    @RunOnVirtualThread
    @Path("/")
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
    @APIResponse(responseCode = "201", description = "User created successfully")
    @APIResponse(responseCode = "400", description = "Invalid input")
    @APIResponse(responseCode = "409", description = "User already exists")
    public Response create(@Valid CreateUserRequest request) {
        logger.info("Creating user with email: {}", request.email());
        User user = createUser.execute(
                request.email(),
                request.firstName(),
                request.lastName(),
                request.password()
        );
        logger.debug("User created with id: {}", user.getId());
        return Response.status(Response.Status.CREATED)
                .entity(userResponseMapper.toResponse(user))
                .build();
    }

    @GET
    @RunOnVirtualThread
    @Path("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique ID")
    @APIResponse(responseCode = "200", description = "User found")
    @APIResponse(responseCode = "404", description = "User not found")
    public Response findById(
            @Parameter(description = "User ID", required = true)
            @PathParam("id") Long id) {
        logger.debug("Fetching user with id: {}", id);
        User user = findUserById.execute(id);
        return Response.ok(userResponseMapper.toResponse(user)).build();
    }

    @GET
    @RunOnVirtualThread
    @Path("/email/{email}")
    @Operation(summary = "Get user by email", description = "Retrieves a user by their email address")
    @APIResponse(responseCode = "200", description = "User found")
    @APIResponse(responseCode = "404", description = "User not found")
    public Response findByEmail(
            @Parameter(description = "User email", required = true)
            @PathParam("email") String email) {
        logger.debug("Fetching user with email: {}", email);
        User usuario = findUserByEmail.execute(email);
        return Response.ok(userResponseMapper.toResponse(usuario)).build();
    }

    @GET
    @RunOnVirtualThread
    @Path("/")
    @Operation(summary = "List all users", description = "Retrieves a list of all users in the system")
    @APIResponse(responseCode = "200", description = "Users retrieved successfully")
    @SecurityRequirement(name = "bearerAuth")
    public Response findAll() {
        logger.debug("Fetching all users");
        List<UserResponse> users = listUsers.execute()
                .stream()
                .map(userResponseMapper::toResponse)
                .collect(Collectors.toList());
        logger.debug("Found {} users", users.size());
        return Response.ok(users).build();
    }

    @PUT
    @RunOnVirtualThread
    @Path("/{id}")
    @Operation(summary = "Update user", description = "Updates user first firstName and last firstName")
    @APIResponse(responseCode = "200", description = "User updated successfully")
    @APIResponse(responseCode = "404", description = "User not found")
    public Response update(
            @Parameter(description = "User ID", required = true)
            @PathParam("id") Long id,
            @Valid UpdateUserRequest request) {
        logger.info("Updating user with id: {}", id);
        User user = updateUser.execute(id, request.firstName(), request.lastName());
        logger.debug("User updated successfully");
        return Response.ok(userResponseMapper.toResponse(user)).build();
    }

    @DELETE
    @RunOnVirtualThread
    @Path("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by their ID")
    @APIResponse(responseCode = "204", description = "User deleted successfully")
    @APIResponse(responseCode = "404", description = "User not found")
    public Response delete(
            @Parameter(description = "User ID", required = true)
            @PathParam("id") Long id) {
        logger.info("Deleting user with id: {}", id);
        deleteUser.execute(id);
        logger.debug("User deleted successfully");
        return Response.noContent().build();
    }
}
