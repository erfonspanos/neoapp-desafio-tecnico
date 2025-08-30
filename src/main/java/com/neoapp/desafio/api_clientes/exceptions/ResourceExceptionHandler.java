package com.neoapp.desafio.api_clientes.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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

@Getter
@Setter
@AllArgsConstructor
class StandardError {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

}