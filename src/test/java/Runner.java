import java.io.*;
import java.net.URI;
import java.util.Scanner;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;

public class Runner {
    private static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(20);

    public static void main(String[] args) throws Exception {
        long t = System.currentTimeMillis();
        for (int i = 0; i < 25; i++) {
            int finalI = i;
            EXECUTORS.submit(() -> pingPong(finalI));
        }
        EXECUTORS.shutdown();
        EXECUTORS.awaitTermination(10, SECONDS);
        System.out.println("total time: " + (System.currentTimeMillis() - t));
    }

    private static void pingPong(int ping) {
        System.out.println("ping " + ping);
        URI base = URI.create("http://localhost:8080/bulkhead/" + ping + "?wait=1000");
        String pong = read(base);
        System.out.println("pong " + pong);
        assert ("pong:" + ping).equals(pong);
    }

    private static String read(URI uri) {
        try (InputStream stream = uri.toURL().openConnection().getInputStream()) {
            return new Scanner(stream).useDelimiter("\\Z").next();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
