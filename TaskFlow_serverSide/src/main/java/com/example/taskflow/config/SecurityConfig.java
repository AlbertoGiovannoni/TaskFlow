package com.example.taskflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.core.userdetails.User;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // TODO implementa baseAuth o Token
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabilita CSRF per semplicità durante lo sviluppo
            .authorizeHttpRequests(requests -> requests
                    .requestMatchers("/api/user/**").authenticated() // Permetti l'accesso a tutti gli endpoint API
                    .requestMatchers("/api/owner/**").hasRole("OWNER") // Solo OWNER può accedere
                    .anyRequest().permitAll())
            .httpBasic(withDefaults()); // Abilita l'autenticazione di base

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Configura utenti e ruoli

        // TODO al momento gli user e gli owner hanno user e password fissi per tutti, migliorare mettendo che ogni utente accede con le sue credenziali e il sistema riconosce se è owner o meno per questa organizzazione
        
        User.UserBuilder users = User.withUsername("user")
            .passwordEncoder(passwordEncoder()::encode); // Usa BCryptPasswordEncoder per codificare la password

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        // Crea un utente con ruolo USER
        manager.createUser(users.username("user").password("password").roles("USER").build());
        // Crea un utente con ruolo OWNER
        manager.createUser(users.username("owner").password("ownerpass").roles("OWNER").build());

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
