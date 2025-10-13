package com.example.demo.NotUsed;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(expressionInterceptUrlRegistry
                        -> expressionInterceptUrlRegistry.anyRequest().permitAll())
                        .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
