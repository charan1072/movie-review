package com.example.movies.implementation;

import com.example.movies.client.AdminServiceClient;
import com.example.movies.service.jwt.IJWTService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;


public class IJWTServiceImpl implements IJWTService {

    private final AdminServiceClient adminServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final String jwtSecretKey;
    private final String username;
    private final String email;
    private final String hashedPassword;


    public IJWTServiceImpl(AdminServiceClient adminServiceClient, PasswordEncoder passwordEncoder, String jwtSecretKey, String username, String email, String hashedPassword) {
        this.adminServiceClient = adminServiceClient;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecretKey = jwtSecretKey;
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
    }


    @Override
    public String generateToken(Long adminId, String username, String role) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
        return Jwts.builder()
                .setClaims(Map.of(
                        "adminId", adminId,
                        "sub", username,
                        "role", role
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    @Override
    public String verifyDummyAdminCredentialsToGenerateToken(String username, String email, String password) {
        boolean isUsernameValid = this.username.equals(username);
        boolean isEmailValid = this.email.equals(email);
        boolean isPasswordValid = passwordEncoder.matches(password, hashedPassword);
        Long adminId = 1L;
        if ((isUsernameValid || isEmailValid) && isPasswordValid) {
            String subject = isUsernameValid ? username : email;
            return generateToken(adminId, subject, "ADMIN");
        }

        return "Invalid Dummy Admin Credentials";
    }

    @Override
    public String verifyAdminCredentialsToGenerateToken(String username, String email, String password) {
        Long adminId = 2L;
        return adminId != null ? generateToken(adminId, username, "ADMIN") : "Invalid Admin Credentials";
    }
}
