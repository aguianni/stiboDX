package com.stibodx.infrastructure.adapter.out.exception;

import com.stibodx.infrastructure.adapter.out.response.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * ExceptionMapper para excepciones de negocio personalizadas
 */
@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {


        ErrorResponse errorResponse = new ErrorResponse(
            exception.getMessage()
        );

        return Response
            .status(Response.Status.BAD_REQUEST)
            .entity(errorResponse)
            .build();
    }
}

