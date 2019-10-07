package com.wedextim.spring.boot.application.openapi;

import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
@CrossOrigin
@ConditionalOnWebApplication
public class OpenAPIController {

    @RequestMapping("/*.json")
    public ResponseEntity<byte[]> get(@RequestParam Map<String, String> allParams, HttpServletRequest request) {
        final String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                return ResponseEntity.notFound().build();
            }
            final byte[] buf = new byte[is.available()];
            is.read(buf);
            return new ResponseEntity<>(buf, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

}
