package org.example.mockify.playlist.controllers;

import org.example.mockify.shared.domain.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ViolationsResponse> handleDomainException(DomainException ex) {
        return ResponseEntity.unprocessableEntity().body(new ViolationsResponse(ex.getViolations()));
    }

    record ViolationsResponse(List<String> violations) {}
}
