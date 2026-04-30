package com.stibodx.infrastructure.adapter.out.exception;

import com.stibodx.domain.exceptions.UserAlreadyExistsException;
import com.stibodx.infrastructure.adapter.out.response.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * ExceptionMapper para excepciones de validación (IllegalArgumentException)
 */
@Provider
public class UserAlreadyExistsExceptionMapper implements ExceptionMapper<UserAlreadyExistsException> {

    @Override
    public Response toResponse(UserAlreadyExistsException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
            exception.getMessage()
        );

        return Response
            .status(Response.Status.CONFLICT)
            .entity(errorResponse)
            .build();
    }
}

