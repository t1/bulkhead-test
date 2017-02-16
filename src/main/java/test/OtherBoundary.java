package test;

import org.slf4j.*;

import javax.ejb.Stateless;
import javax.ws.rs.*;

@Path("/other")
@Stateless
public class OtherBoundary {
    private static final Logger log = LoggerFactory.getLogger(OtherBoundary.class);

    @GET
    public String get() throws InterruptedException {
        log.debug("other");
        return "other-pong";
    }
}
