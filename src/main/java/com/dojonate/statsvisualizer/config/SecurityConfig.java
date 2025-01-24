package com.dojonate.statsvisualizer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").authenticated() // Restrict /admin/** to authenticated users
                        .anyRequest().permitAll() // Allow all other endpoints
                )
                .httpBasic(Customizer.withDefaults()); // Enable basic authentication with default settings

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Configure an in-memory user for authentication
        UserDetails admin = User
                .withUsername("maintainer")
                .password("{noop}password") // `{noop}` disables password encoding for simplicity
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
