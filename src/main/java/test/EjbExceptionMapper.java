package test;

import org.slf4j.*;

import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

import static javax.ws.rs.core.Response.Status.*;

@Provider
public class EjbExceptionMapper implements ExceptionMapper<EJBException> {
    private static final Logger log = LoggerFactory.getLogger(EjbExceptionMapper.class);

    @Override public Response toResponse(EJBException exception) {
        log.error("map ejb exception: {}", exception.getMessage());
        return Response.status(SERVICE_UNAVAILABLE).entity("failed:" + exception.getMessage()).build();
    }
}
