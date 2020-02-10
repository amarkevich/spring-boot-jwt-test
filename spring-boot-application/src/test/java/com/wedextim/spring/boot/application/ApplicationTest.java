package com.wedextim.spring.boot.application;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.wedextim.spring.boot.application.controller.Controller;
import com.wedextim.spring.boot.application.controller.ControllerPreAuthorize;
import com.wedextim.spring.boot.application.model.UserRole;
import com.wedextim.spring.boot.test.ServerPortInitializer;
import com.wedextim.spring.boot.test.jwt.JwtTestSupport;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = Application.class,
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = {
        "debug=true",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost",
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:${server.port}/jwks.json"
    }
)
@ContextConfiguration(initializers = ServerPortInitializer.class)
public class ApplicationTest {

    private static final UUID USER_ID = UUID.randomUUID();

    @LocalServerPort
    private int port;

    @Test
    public void getPlain() throws Exception {
        assertThrows(NotAuthorizedException.class,
            () -> controller(null).getPlain());

        assertNotNull(controller(USER_ID, (UserRole[]) null).getPlain());
        assertNotNull(controller(USER_ID).getPlain());
    }

    @Test
    public void getJson() throws Exception {
        assertThrows(NotAuthorizedException.class,
            () -> controller(null).getJson());

        assertTrue(controller(USER_ID).getJson().length > 0);
    }

    @Test
    public void getPlainPreAuthorize() throws Exception {
        assertThrows(NotAuthorizedException.class,
            () -> controllerPreAuthorize(null).getPlain());

        assertThrows(ForbiddenException.class,
            () -> controllerPreAuthorize(USER_ID).getPlain());

        assertNotNull(controllerPreAuthorize(USER_ID, UserRole.USER).getPlain());
    }

    @Test
    public void getJsonPreAuthorize() throws Exception {
        assertThrows(NotAuthorizedException.class,
            () -> controllerPreAuthorize(null).getJson());

        assertThrows(ForbiddenException.class,
            () -> controllerPreAuthorize(USER_ID).getJson());

        assertTrue(controllerPreAuthorize(USER_ID, UserRole.USER).getJson().length > 0);
    }

    @Test
    public void openApi() throws IOException {
        try (InputStream is = new URL("http://localhost:" + port + "/openapi.json").openStream()) {
            String openapi = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
            assertNotNull(openapi);
        }
    }

    private Controller controller(UUID userId, UserRole...roles) throws Exception {
        final Controller sentencesController = JAXRSClientFactory.create("http://localhost:" + port,
            Controller.class, Collections.singletonList(new JacksonJsonProvider())/*,
            Collections.singletonList(new org.apache.cxf.feature.LoggingFeature()), null*/);
        applyRoles(sentencesController, userId, roles);
        return sentencesController;
    }

    private ControllerPreAuthorize controllerPreAuthorize(UUID userId, UserRole...roles) throws Exception {
        final ControllerPreAuthorize sentencesController = JAXRSClientFactory.create("http://localhost:" + port,
            ControllerPreAuthorize.class, Collections.singletonList(new JacksonJsonProvider())/*,
            Collections.singletonList(new org.apache.cxf.feature.LoggingFeature()), null*/);
        applyRoles(sentencesController, userId, roles);
        return sentencesController;
    }

    private static void applyRoles(Object client, UUID userId, UserRole...roles) throws Exception {
        if (userId != null) {
            WebClient.client(client).authorization(JwtTestSupport.bearerToken(userId,
                null != roles ? Arrays.stream(roles).map(UserRole::name).toArray(String[]::new) : null));
        }
    }
}
