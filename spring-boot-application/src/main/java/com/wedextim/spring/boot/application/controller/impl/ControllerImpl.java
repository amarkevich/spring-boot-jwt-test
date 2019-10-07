package com.wedextim.spring.boot.application.controller.impl;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.wedextim.spring.boot.application.controller.Controller;

@RestController
@CrossOrigin
public class ControllerImpl implements Controller {

    private static final String[] ELEMENTS = {"1", "2", "3"};

    @Override
    public String getPlain() {
        return String.join(",", ELEMENTS);
    }

    @Override
    public String[] getJson() {
        return ELEMENTS;
    }

}
