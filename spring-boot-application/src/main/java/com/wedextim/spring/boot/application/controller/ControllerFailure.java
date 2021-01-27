package com.wedextim.spring.boot.application.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/failure")
public interface ControllerFailure {

    String FAILURE_MESSSAGE = "failure message";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    String getFailure();

}
