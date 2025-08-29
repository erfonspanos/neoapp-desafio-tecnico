package com.neoapp.desafio.api_clientes.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice // Anotação para interceptar exceções nos controllers
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) // Captura esta exceção específica
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        String error = "Recurso não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}

// Crie esta classe auxiliar, pode ser dentro do mesmo arquivo ou em um novo
class StandardError {
    public Instant timestamp;
    public Integer status;
    public String error;
    public String message;
    public String path;
    // Construtor, getters e setters
    public StandardError(Instant timestamp, Integer status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}