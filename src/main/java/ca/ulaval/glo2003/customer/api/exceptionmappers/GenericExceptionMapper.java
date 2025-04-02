package ca.ulaval.glo2003.customer.api.exceptionmappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * Convertit les exceptions générales en réponses HTTP appropriées
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "Une erreur inattendue s'est produite: " + exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}