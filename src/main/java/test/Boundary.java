package test;

import org.slf4j.*;

import javax.ejb.Stateless;
import javax.ws.rs.*;

@Path("/")
@Stateless
public class Boundary {
    private static final Logger log = LoggerFactory.getLogger(Boundary.class);

    @GET
    @Path("/{ping}")
    public String get(@PathParam("ping") String ping, @QueryParam("wait") int wait) throws InterruptedException {
        log.debug("ping: {}", ping);
        Thread.sleep(wait);
        log.debug("pong: {}", ping);
        return "pong:" + ping;
    }
}
