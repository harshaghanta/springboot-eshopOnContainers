package com.eshoponcontainers.catalogapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public ResponseEntity index() {
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).header("Location", "/swagger-ui/index.html").build();
    }
}
