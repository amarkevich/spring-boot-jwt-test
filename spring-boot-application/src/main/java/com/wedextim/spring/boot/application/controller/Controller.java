package com.wedextim.spring.boot.application.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@RequestMapping("/v1")
@OpenAPIDefinition(
    info = @Info(title = "Controller", version = "1"),
    servers = @Server(url = "/controller")
)
@SecurityScheme(
    name = "cognito-authorizer",
    paramName = HttpHeaders.AUTHORIZATION,
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER
)
@Path("/v1")
public interface Controller {

    @GetMapping(produces = MediaType.TEXT_PLAIN)
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
        summary = "Get TEXT_PLAIN",
        responses = {
            @ApiResponse(responseCode = "200", description = "plain"),
        },
        security = @SecurityRequirement(name = "cognito-authorizer")
    )
    String getPlain();

    @GetMapping
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get APPLICATION_JSON",
        responses = {
            @ApiResponse(responseCode = "200", description = "json"),
        },
        security = @SecurityRequirement(name = "cognito-authorizer")
    )
    String[] getJson();

}
