package com.login.register.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUserName(String token);
    boolean isValidToken(String token, UserDetails userDetails);
    String generateToken(UserDetails userDetails);
    Object generateRefereshToken(Map<String, Object> extraClaims, UserDetails userDetails);
}
