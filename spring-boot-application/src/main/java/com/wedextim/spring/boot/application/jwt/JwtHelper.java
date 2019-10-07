package com.wedextim.spring.boot.application.jwt;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.HttpClientErrorException;

import com.wedextim.spring.boot.application.model.UserRole;

public final class JwtHelper {

    private JwtHelper() {
    }

    protected UUID getLoggedInUser() {
        return UUID.fromString(getToken().getSubject());
    }

    protected Set<UserRole> getLoggedInUserRoles() {
        return getToken().getClaimAsStringList("cognito:groups").stream().map(UserRole::valueOf)
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(UserRole.class)));
    }

    private Jwt getToken() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken)) {
            throw HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, null, null, null, null);
        }
        return ((JwtAuthenticationToken) authentication).getToken();
    }

}
