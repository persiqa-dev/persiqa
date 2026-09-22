package com.persiqa.web;

import com.persiqa.application.ScopeAccessService.UnknownScopeException;
import com.persiqa.core.ScopeAccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Stable HTTP translation for shared application failures. */
@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ProblemDetail> invalidRequest(IllegalArgumentException error) {
    return response(HttpStatus.BAD_REQUEST, error);
  }

  @ExceptionHandler(ScopeAccessDeniedException.class)
  public ResponseEntity<ProblemDetail> forbidden(ScopeAccessDeniedException error) {
    return response(HttpStatus.FORBIDDEN, error);
  }

  @ExceptionHandler(UnknownScopeException.class)
  public ResponseEntity<ProblemDetail> missingScope(UnknownScopeException error) {
    return response(HttpStatus.NOT_FOUND, error);
  }

  private static ResponseEntity<ProblemDetail> response(HttpStatus status, RuntimeException error) {
    return ResponseEntity.status(status)
        .body(ProblemDetail.forStatusAndDetail(status, error.getMessage()));
  }
}
