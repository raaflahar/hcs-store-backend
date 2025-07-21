package com.raaflahar.hcs_idn.util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.raaflahar.hcs_idn.entity.User;

@Component
public class JwtUtil {
    @Value("${spring.application.name}")
    private String appName;

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.access.token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.token.expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(String username){
        Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey.getBytes(StandardCharsets.UTF_8));

        return JWT.create()
                .withIssuer(appName)
                .withSubject(username)
                .withExpiresAt(Instant.now().plusSeconds(accessTokenExpiration))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public String generateRefreshToken(String username) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
        return JWT.create()
                .withIssuer(appName)
                .withSubject(username)
                .withExpiresAt(Instant.now().plusSeconds(refreshTokenExpiration))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public boolean validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(appName).build();
            verifier.verify(token);

            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
