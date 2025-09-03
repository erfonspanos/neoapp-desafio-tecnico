package com.neoapp.desafio.api_clientes.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.neoapp.desafio.api_clientes.exceptions.StandardError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 1. Define o status HTTP da resposta como 403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        // 2. Cria o nosso objeto de erro padrão para acesso negado
        String errorMessage = "Acesso negado. Você não tem permissão para acessar este recurso.";
        StandardError err = new StandardError(
                Instant.now(),
                HttpStatus.FORBIDDEN.value(),
                "Acesso Negado",
                errorMessage,
                request.getRequestURI()
        );

        // 3. Converte o objeto de erro para JSON e escreve na resposta
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        response.getWriter().write(mapper.writeValueAsString(err));
    }
}