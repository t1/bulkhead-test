import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;

public class Runner {
    private static final ExecutorService EXECUTORS = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        List<Future<String>> futures = new ArrayList<>();
        long t = System.currentTimeMillis();
        for (int i = 0; i < 25; i++) {
            int finalI = i;
            futures.add(EXECUTORS.submit(() -> pingPong(finalI)));
        }
        EXECUTORS.shutdown();
        System.out.println("results: ");
        for (Future<String> future : futures)
            System.out.println("-> " + future.get(10, SECONDS));
        EXECUTORS.awaitTermination(1, MINUTES);
        System.out.println("total time: " + (System.currentTimeMillis() - t));
    }

    private static String pingPong(int ping) {
        // System.out.println("ping " + ping);
        URI base = URI.create("http://localhost:8080/bulkhead/" + ping + "?wait=1000");
        try {
            String pong = read(base);
            // System.out.println("pong " + pong);
            assert ("pong:" + ping).equals(pong);
            return ping + ":ok";
        } catch (IOException e) {
            return ping + ":failed: " + e.getMessage();
        }
    }

    private static String read(URI uri) throws IOException {
        try (InputStream stream = uri.toURL().openConnection().getInputStream()) {
            return new Scanner(stream).useDelimiter("\\Z").next();
        }
    }
}
