package com.eshoponcontainers.webhooksclient.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eshoponcontainers.webhooksclient.Settings;

import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
public class CheckController {

    private final Settings settings;
    private static final String WEBHOOKCHECKHEADER = "X-eshop-whtoken";

    @RequestMapping(value =  "/check", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> check(@RequestHeader(WEBHOOKCHECKHEADER) String token) {
        //extract header webhookscheckheader from request

        if(settings.isValidateToken() || token.equals(settings.getToken())) {
            if(!settings.getToken().isEmpty()){
                return ResponseEntity.ok().header(WEBHOOKCHECKHEADER, settings.getToken()).build();
            }
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }

    @PostMapping("/checkpost")
    public ResponseEntity<?> checkpost() {
        if(settings.isValidateToken()) {
            if(!settings.getToken().isEmpty()){
                return ResponseEntity.ok().header(WEBHOOKCHECKHEADER, settings.getToken()).build();
            }
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
    
}
