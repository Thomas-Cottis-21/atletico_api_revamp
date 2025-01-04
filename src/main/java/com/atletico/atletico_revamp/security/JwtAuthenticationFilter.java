package com.atletico.atletico_revamp.security;

import com.atletico.atletico_revamp.config.JwtConfigurationProperties;
import com.atletico.atletico_revamp.service.JwtService;
import com.atletico.atletico_revamp.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfigurationProperties jwtConfigurationProperties;
    private final JwtService jwtService;
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    public JwtAuthenticationFilter(JwtConfigurationProperties jwtConfigurationProperties, JwtService jwtService, UserService userService) {
        this.jwtConfigurationProperties = jwtConfigurationProperties;
        this.jwtService = jwtService;
        this.userService = userService;
    }

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
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        log.info("Logging from filter");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = this.jwtService.extractUsername(token);
            log.info("Username " + username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userService.loadUserByUsername(username);
            log.info("Getting user from database " + userDetails);

            if (this.jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                log.info("Username valid and token is valid");
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                log.info("Token not valid " + token);
            }
        } else {
            log.info("Username or token invalid");
        }

        filterChain.doFilter(request, response);
    }

}
