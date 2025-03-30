package com.example.movies.service;


import com.example.movies.client.AdminServiceClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Autowired
    private AdminServiceClient adminServiceClient;
        public boolean isTokenExpired(String jwt) {
        return extractClaims(jwt).getExpiration().after(new Date());
    }

    public String extractrole(String jwt){
      return extractClaims(jwt).get("role", String.class);
      }

    public boolean isAdminValid(Long adminId) {
        try {
           Boolean isAdminValid = adminServiceClient.isAdminValid(adminId);

            return isAdminValid != null && isAdminValid;
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractAdminId(String token) {
        return extractClaims(token).get("adminId", Long.class);
    }
    public Claims extractClaims(String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }


}
