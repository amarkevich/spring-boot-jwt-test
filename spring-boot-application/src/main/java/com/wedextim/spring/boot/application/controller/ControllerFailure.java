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

@RequestMapping("/failure")
@OpenAPIDefinition(
    info = @Info(title = "ControllerFailure", version = "1"),
    servers = @Server(url = "/controller")
)
@SecurityScheme(
    name = "cognito-authorizer",
    paramName = HttpHeaders.AUTHORIZATION,
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER
)
@Path("/failure")
public interface ControllerFailure {

    String FAILURE_MESSSAGE = "failure message";

    @GetMapping(produces = MediaType.APPLICATION_JSON)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get",
        responses = {
            @ApiResponse(responseCode = "422", description = "failure"),
        },
        security = @SecurityRequirement(name = "cognito-authorizer")
    )
    String getFailure();

}
