package com.stibodx.infrastructure.adapter.out.exception;

import com.stibodx.domain.exceptions.InvalidEmailException;
import com.stibodx.infrastructure.adapter.out.response.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * ExceptionMapper para excepciones de validación (IllegalArgumentException)
 */
@Provider
public class InvalidEmailExceptionMapper implements ExceptionMapper<InvalidEmailException> {

    @Override
    public Response toResponse(InvalidEmailException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
            exception.getMessage()
        );

        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(errorResponse)
            .build();
    }
}

