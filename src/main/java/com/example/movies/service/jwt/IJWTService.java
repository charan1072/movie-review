package com.example.movies.service.jwt;

import org.springframework.stereotype.Service;

public interface IJWTService {

    String generateToken(Long adminId, String username, String role);

    String verifyDummyAdminCredentialsToGenerateToken(String username, String email, String password);

    String verifyAdminCredentialsToGenerateToken(String username, String email, String password);
}
