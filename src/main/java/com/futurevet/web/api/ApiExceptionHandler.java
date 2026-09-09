package com.futurevet.web.api;

import com.futurevet.service.RegraException;
import com.futurevet.web.api.dto.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice(basePackages = "com.futurevet.web.api")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler {

    @ExceptionHandler(RegraException.class)
    public ResponseEntity<ApiError> regra(RegraException e) {
        return ResponseEntity.badRequest().body(new ApiError(400, e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> credenciais(BadCredentialsException e) {
        return ResponseEntity.status(401).body(new ApiError(401, e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> negado(AccessDeniedException e) {
        return ResponseEntity.status(403)
                .body(new ApiError(403, "Você não tem permissão para acessar este registro."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> conflito(DataIntegrityViolationException e) {
        return ResponseEntity.status(409).body(new ApiError(409,
                "Não foi possível salvar: há um registro duplicado ou dados vinculados."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacao(MethodArgumentNotValidException e) {
        List<String> detalhes = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(400, "Dados inválidos.", detalhes));
    }
}