package com.raaflahar.hcs_idn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.raaflahar.hcs_idn.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final UserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req.requestMatchers(
                                                "/v3/api-docs/**",
                                                "/api-docs/**",
                                                "/swagger-ui/**",
                                                "/swagger-ui.html")
                                                .permitAll()
                                                .requestMatchers("/api/auth/**").permitAll()
                                                // User Management
                                                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/api/users/**").hasAnyRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/api/users/**")
                                                .hasAnyRole("ADMIN", "USER") // User can update own profile
                                                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyRole("ADMIN")
                                                // Customer Management
                                                .requestMatchers(HttpMethod.PUT, "/api/customers/**")
                                                .hasAnyRole("ADMIN", "USER", "CUSTOMER") // Customer can update own
                                                .requestMatchers(HttpMethod.GET, "/api/customers/reports/**")
                                                .hasAnyRole("ADMIN", "CUSTOMER") // Customer can download own report
                                                .requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "USER")
                                                // Product Management
                                                .requestMatchers("/api/products/**").hasAnyRole("ADMIN", "USER")
                                                // Tax Management
                                                .requestMatchers("/api/taxes/**").hasAnyRole("ADMIN")
                                                // Transaction Management
                                                .requestMatchers("/api/transactions/**").hasAnyRole("ADMIN", "USER")
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}