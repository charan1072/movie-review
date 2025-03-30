package com.example.movies.controller;

import com.example.movies.aspect.annotations.SetRequestAttributes;
import com.example.movies.service.jwt.IJWTService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/admin")
public class JwtController {


    private final IJWTService jwtService;

    @Autowired
    public JwtController(IJWTService jwtService) {
        this.jwtService = jwtService;
    }


    @SetRequestAttributes
    @GetMapping(value = "/dummy/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyDummyAdminCredentialsToGenerateToken(@RequestParam String username, @RequestParam String email, @RequestParam @NotBlank String password) {
        String token = jwtService.verifyDummyAdminCredentialsToGenerateToken(username, email, password);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @SetRequestAttributes
    @GetMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyAdminCredentialsToGenerateToken(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        String token = jwtService.verifyAdminCredentialsToGenerateToken(username, email, password);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}