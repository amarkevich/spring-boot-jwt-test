package com.wedextim.spring.boot.application.controller.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.wedextim.spring.boot.application.controller.ControllerPreAuthorize;

@RestController
@CrossOrigin
public class ControllerPreAuthorizeImpl implements ControllerPreAuthorize {

    private static final String[] ELEMENTS = {"1", "2", "3"};

    @Override
    @PreAuthorize("hasAuthority('USER')")
    public String getPlain() {
        return String.join(",", ELEMENTS);
    }

    @Override
    @PreAuthorize("hasAuthority('USER')")
    public String[] getJson() {
        return ELEMENTS;
    }

}
