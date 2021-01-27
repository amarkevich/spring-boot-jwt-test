package com.wedextim.spring.boot.application.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wedextim.spring.boot.application.controller.ControllerFailure;
import com.wedextim.spring.boot.application.controller.ControllerPreAuthorize;

@RestController
@CrossOrigin
public class ControllerPreAuthorizeImpl implements ControllerPreAuthorize, ControllerFailure {

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

    @Override
    public String getFailure() {
        throw new ProcessingException(FAILURE_MESSSAGE);
    }

    @SuppressWarnings("serial")
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public static class ProcessingException extends RuntimeException {

        public ProcessingException(String message) {
            super(message);
        }

    }

}
