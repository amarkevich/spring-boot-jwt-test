package com.wedextim.spring.boot.application.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wedextim.spring.boot.application.controller.ControllerFailure;


@RestController
@CrossOrigin
public class ControllerFailureImpl implements ControllerFailure {

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
