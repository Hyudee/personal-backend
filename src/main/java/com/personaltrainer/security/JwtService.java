package com.personaltrainer.security;

import com.personaltrainer.user.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_USER_ID = "userId";

    private final JwtProperties properties;

    private SecretKey key;

    @PostConstruct
    void init(){
        if (properties.jwtSecret() == null || properties.jwtSecret().length() < 32){
            throw new IllegalStateException(
                    "app.security.jwt-secret precisa ter pelo menos 32 caracteres. " +
                    "Configure em application-local.properties ou na variavel JWT_SECRET");
        }
        this.key = Keys.hmacShaKeyFor(properties.jwtSecret().getBytes());
    }

    public String generateToken (User user) {
        Instant now = Instant.now();
        long minutes = properties.jwtExpirationMinutes() > 0 ? properties.jwtExpirationMinutes() : 1440;

        return Jwts.builder()
                .subject(user.getEmail())
                .claim(CLAIM_ROLE, user.getRole().name())
                .claim(CLAIM_USER_ID, user.getId())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(minutes, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    // Se o token n for valido, ele joga JwtException
    public Claims parseClaims (String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail (String token) {
        return parseClaims(token).getSubject();
    }

    public Long extractUserId (String token) {
        return parseClaims(token).get(CLAIM_USER_ID, Long.class)
    }

}
