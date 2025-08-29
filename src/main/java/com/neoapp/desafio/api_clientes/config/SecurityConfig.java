package com.neoapp.desafio.api_clientes.config; // Verifique se o pacote está correto

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Desabilita o CSRF, pois a API será stateless (não usará sessões)
                .csrf(AbstractHttpConfigurer::disable)
                // Define as regras de autorização
                .authorizeHttpRequests(authorize -> authorize
                        // PERMITE ACESSO A TODOS OS ENDPOINTS SEM AUTENTICAÇÃO
                        .requestMatchers("/**").permitAll()
                );
        return http.build();
    }
}