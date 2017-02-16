import org.slf4j.*;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;

public class Runner {
    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    private static final URI BASE = URI.create("http://localhost:8080/bulkhead/");
    private static final ExecutorService EXECUTORS = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        List<Future<String>> futures = new ArrayList<>();
        long t = System.currentTimeMillis();
        for (int i = 1; i <= 25; i++)
            futures.add(submit(i));
        Thread.sleep(500);
        log.info("other: {}", read(BASE.resolve("other" + "?wait=1000")));
        EXECUTORS.shutdown();
        log.info("results: ");
        for (Future<String> future : futures)
            log.info("-> {}", future.get(10, SECONDS));
        EXECUTORS.awaitTermination(1, MINUTES);
        log.info("total time: {}", (System.currentTimeMillis() - t));
    }

    private static Future<String> submit(int id) {
        return EXECUTORS.submit(() -> {
            log.info("ping {}", id);
            try {
                String pong = read(BASE.resolve("ping/" + id + "?wait=1000"));
                log.info("pong {}", pong);
                assert ("pong:" + id).equals(pong);
                return id + ":ok";
            } catch (IOException e) {
                return id + ":failed: " + e.getMessage();
            }
        });
    }

    private static String read(URI uri) throws IOException {
        try (InputStream stream = uri.toURL().openConnection().getInputStream()) {
            return new Scanner(stream).useDelimiter("\\Z").next();
        }
    }
}
