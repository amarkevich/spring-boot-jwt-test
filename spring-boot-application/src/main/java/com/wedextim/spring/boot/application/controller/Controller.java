package com.wedextim.spring.boot.application.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1")
public interface Controller {

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    String getPlain();

    @GetMapping
    String[] getJson();

}
