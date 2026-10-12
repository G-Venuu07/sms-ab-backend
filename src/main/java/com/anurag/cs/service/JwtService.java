package com.anurag.cs.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
      private String secretKey = "mysecretkey12345678mysecretkey12345678";

      private SecretKey getKey() {
            return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
      }

      public String generateToken(String email) {
            return Jwts.builder()
                        .subject(email)
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                        .signWith(getKey())
                        .compact();
      }

      public String extractUsername(String token) {
            return Jwts.parser()
                        .verifyWith(getKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject();// extract your email
      }

      public boolean validateToken(String token) {
            try {
                  Jwts.parser()
                              .verifyWith(getKey())
                              .build()
                              .parseSignedClaims(token);
                  return true;
            } catch (Exception e) {
                  return false;
            }
      }
}
