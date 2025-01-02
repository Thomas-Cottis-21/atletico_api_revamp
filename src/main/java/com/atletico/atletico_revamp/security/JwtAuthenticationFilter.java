package com.atletico.atletico_revamp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Autowired
    public JwtAuthenticationFilter() {
    }

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // doFilterInternal performs filtering logic in the request processing pipeline
    // It is invoked during each HTTP request (basically middleware)
    // Extracts JWT from request
    // Validates JWT token
    // Sets the authentication in the security context if token is valid
    // request - Incoming HTTP request
    // response - The response object that can be used to send a response back to the client
    // filterChain - The chain of filters that the request will pass through AFTER the current filter is executed
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Extracts token
        String token = resolveToken(request);
        logger.info(token);
        if (token != null && validateToken(token)) {
            Authentication auth = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            logger.info("TOKEN: {}", token);
            logger.info("SECRET_KEY{}", SECRET_KEY);
            // Token validation logic
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Authentication getAuthentication(String token) {
        // Extract user details from token and create authentication object
        Claims claims = getClaimsFromToken(token);
        String username = claims.getSubject();
        // Principal - typically the unique identifier of the user
        // Credentials - typically the password or representation of the credentials;
        // Can be null because token is issued instead
        // Authorities (optional) - collection of granted authorities (roles, permissions) that the user has
        return new UsernamePasswordAuthenticationToken(username, null);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
