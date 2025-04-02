package ca.ulaval.glo2003.customer.api.exceptionmappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * Convertit les IllegalArgumentException en réponses HTTP 400 Bad Request
 */
@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}