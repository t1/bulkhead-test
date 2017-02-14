package test;

import javax.ejb.Stateless;
import javax.ws.rs.*;

@Path("/")
@Stateless
public class Boundary {
    @GET
    @Path("/{ping}")
    public String get(@PathParam("ping") String ping, @QueryParam("wait") int wait) throws InterruptedException {
        // System.out.println("ping:" + ping);
        Thread.sleep(wait);
        // System.out.println("pong:" + ping);
        return "pong:" + ping;
    }
}
