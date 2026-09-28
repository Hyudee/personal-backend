package com.personaltrainer.security;

import org.springframework.boot.context.properties.ConfigurationProperties;


//Defina app.security.jwt-secret em application-local.properties (nunca no Git) ou na variável JWT_SECRET
// No minimo 32 caracteres

@ConfigurationProperties (prefix = "app.security")
public record JwtProperties (
        String jwtSecret,
        long jwtExpirationMinutes) {
}
