package com.plnv.forum.handlers;

import com.plnv.forum.model.Response;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .reason(Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                        .build()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Response> handleIllegalStateException(IllegalStateException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .reason(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .reason(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({NoSuchElementException.class, InputMismatchException.class})
    public ResponseEntity<Response> handleNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .reason(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Response> handleSQLException(SQLException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .reason(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<Response> handleHttpMessageNotWritableException(HttpMessageNotWritableException e) {
        log.error(e.getMessage());
        return ResponseEntity.internalServerError().body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .reason(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Response> handleJwtException(JwtException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .reason(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler({UsernameNotFoundException.class, AuthenticationException.class})
    public ResponseEntity<Response> handleAuthenticationException(AuthenticationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .reason(e.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Response.builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .reason(e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; ")))
                        .build()
        );
    }
}
