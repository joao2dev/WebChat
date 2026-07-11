package com.example.chatweb.Config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.chatweb.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;
    public String generateToken(User user){
        Algorithm algorithm = Algorithm.HMAC256(secret);
       return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("chatweb")
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .sign(algorithm);

    }
    public Optional<JWTUserData> validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String username = JWT.require(algorithm)
                    .withIssuer("chatweb")
                    .build()
                    .verify(token)
                    .getSubject();
            return Optional.of(new JWTUserData(username));
        }catch (JWTVerificationException exception){
            return Optional.empty();
        }
    }
    }

