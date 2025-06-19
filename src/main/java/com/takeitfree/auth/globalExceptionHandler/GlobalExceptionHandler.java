package com.takeitfree.auth.globalExceptionHandler;

import com.takeitfree.auth.exceptions.ObjectNotValidException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<RepresentationException> handleObjectNotValidException(ObjectNotValidException ex) {
        RepresentationException re = RepresentationException.builder()
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RepresentationException> handleEntityNotFoundException(EntityNotFoundException ex) {
        RepresentationException re = RepresentationException.builder()
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<RepresentationException> handleEntityExistsException(EntityExistsException ex) {
        RepresentationException re = RepresentationException.builder()
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RepresentationException> handleConstraintViolationException(ConstraintViolationException ex) {
        RepresentationException re = RepresentationException.builder()
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RepresentationException> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        RepresentationException re = RepresentationException.builder()
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RepresentationException> handleNoResourceFoundException(NoResourceFoundException ex) {
        RepresentationException re = RepresentationException.builder()
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RepresentationException> handleBadCredentialsException(BadCredentialsException ex) {
        RepresentationException re = RepresentationException.builder()
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<RepresentationException> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        RepresentationException re = RepresentationException.builder()
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(re);
    }
}
