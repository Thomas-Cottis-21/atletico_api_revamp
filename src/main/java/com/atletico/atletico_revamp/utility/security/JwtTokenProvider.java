package com.atletico.atletico_revamp.utility.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.List;

public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    @Value("${jwt.validity}")
    private long VALIDITY_IN_MS;

    public String createToken(String username, List<String> roles) {
        // Generate secret key object
        byte[] secretKeyBytes = SECRET_KEY.getBytes();
        Key key = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("iat", System.currentTimeMillis());
        claims.put("exp", System.currentTimeMillis() + VALIDITY_IN_MS);

        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MS);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
