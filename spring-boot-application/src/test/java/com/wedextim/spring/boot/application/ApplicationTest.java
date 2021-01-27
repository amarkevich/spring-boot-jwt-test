package com.wedextim.spring.boot.application;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = Application.class,
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = {
        "debug=true"
    }
)
public class ApplicationTest {

    @LocalServerPort
    private int port;

    @Test
    public void getPlain() throws Exception {
        URL url = new URL("http://localhost:" + port + "/v1");
        HttpURLConnection urlConnection = (HttpURLConnection)  url.openConnection();
        urlConnection.setRequestProperty("Accept", MediaType.TEXT_PLAIN_VALUE);
        try (InputStream in = urlConnection.getInputStream()) {
            String r = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
            System.out.println(r);
        }
    }

    @Test
    public void getJson() throws Exception {
        URL url = new URL("http://localhost:" + port + "/v1");
        HttpURLConnection urlConnection = (HttpURLConnection)  url.openConnection();
        urlConnection.setRequestProperty("Accept", MediaType.APPLICATION_JSON_VALUE);
        try (InputStream in = urlConnection.getInputStream()) {
            String r = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
            System.out.println(r);
        }
    }

    @Test
    public void getFailure() throws Exception {
        URL url = new URL("http://localhost:" + port + "/failure");
        HttpURLConnection urlConnection = (HttpURLConnection)  url.openConnection();
        urlConnection.setRequestProperty("Accept", MediaType.APPLICATION_JSON_VALUE);
        try (InputStream in = urlConnection.getInputStream()) {
            fail();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("422"));
        }
    }

}
