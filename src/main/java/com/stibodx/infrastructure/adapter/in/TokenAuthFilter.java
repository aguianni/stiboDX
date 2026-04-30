package com.stibodx.infrastructure.adapter.in;

import com.stibodx.infrastructure.adapter.out.response.ErrorResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Provider
public class TokenAuthFilter implements ContainerRequestFilter {

    @ConfigProperty(name = "quarkus.api.token")
    String expectedToken;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.equals("Bearer " + expectedToken)) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity(new ErrorResponse("Token invalid"))
                            .build()
            );
        }
    }
}
