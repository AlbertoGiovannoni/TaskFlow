package com.example.taskflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.taskflow.DomainModel.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // TODO implementa baseAuth o Token
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabilita CSRF per semplicita durante lo sviluppo
            .authorizeHttpRequests(requests -> requests
                    .requestMatchers("/api/user/**").permitAll() // Permetti l'accesso a tutti gli endpoint API
                    .anyRequest().permitAll())
            .httpBasic(basic -> basic.disable()); // Disabilita l'autenticazione di base

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
