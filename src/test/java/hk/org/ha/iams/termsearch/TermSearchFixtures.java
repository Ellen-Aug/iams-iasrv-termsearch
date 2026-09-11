package hk.org.ha.iams.termsearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

final class TermSearchFixtures {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TermSearchFixtures() {
    }

    static String request(String fixturePath) {
        return read(fixturePath + ".request.json");
    }

    static JsonNode expected(String fixturePath) {
        try {
            return MAPPER.readTree(read(fixturePath + ".expected.json"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static String read(String classpathRelative) {
        String resource = "/fixtures/" + classpathRelative;
        try (InputStream in = TermSearchFixtures.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("Missing classpath resource " + resource);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
