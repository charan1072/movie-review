package com.example.movies.configuration;

import com.example.movies.client.AdminServiceClient;
import com.example.movies.implementation.IJWTServiceImpl;
import com.example.movies.service.jwt.IJWTService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MovieConfiguration {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IJWTService ijwtService(
            @Qualifier("adminServiceClientImpl") AdminServiceClient adminServiceClient,
            PasswordEncoder passwordEncoder,
            @Value("${jwt.secret}") String jwtSecretKey,
            @Value("${jwt.username}") String username,
            @Value("${jwt.email}") String email,
            @Value("${jwt.hash-password}") String hashedPassword
    ) {
        return new IJWTServiceImpl(
                adminServiceClient, passwordEncoder,
                jwtSecretKey, username, email, hashedPassword
        );
    }
}
