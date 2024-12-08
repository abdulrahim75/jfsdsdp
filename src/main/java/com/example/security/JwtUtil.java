package com.example.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long ONE_DAY = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    public JwtUtil() {
        // In production, this should be stored securely and not in code
        String secretString = "your_very_long_and_secure_secret_key_that_is_at_least_256_bits";
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public String generateToken(String username, String role, String roleSpecificId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("roleSpecificId", roleSpecificId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ONE_DAY))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
