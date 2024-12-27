package com.atletico.atletico_revamp.config;

import com.atletico.atletico_revamp.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Spring Security Configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // HttpSecurity allows configuring how filters are applied to Http endpoints
        http
            // CSRF - Cross-Site Request Forgery is disabled (JWTs "mitigate" CSRF attacks)
            .csrf(AbstractHttpConfigurer::disable)
            // Setting session management to stateless (JWTs are inherently stateless)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Defining only public endpoints to be permitted without token
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/auth/login", "/auth/register").permitAll()
                    // All other endpoints require JWT authorization
                    .anyRequest().authenticated()
            )
            // Adding custom JWT filter
            .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
