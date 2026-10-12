package com.personaltrainer.common;

import com.personaltrainer.accountdraft.EmailAlreadyInUseException;
import com.personaltrainer.accountdraft.InvalidPaymentDayException;
import com.personaltrainer.auth.DraftExpiredException;
import com.personaltrainer.auth.DraftRejectedException;
import com.personaltrainer.auth.PendingApprovalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler (BadCredentialsException.class)
    public ResponseEntity <Map<String, String>> handleBadCredentials(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of ("erro", ex.getMessage()));
    }

    @ExceptionHandler (PendingApprovalException.class)
    public ResponseEntity <Map<String, String>> handlePendingApproval(PendingApprovalException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of ("erro", ex.getMessage()));
    }

    @ExceptionHandler (DraftRejectedException.class)
    public ResponseEntity <Map<String, String>> handleDraftRejected(DraftRejectedException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of ("erro", ex.getMessage()));
    }

    @ExceptionHandler (DraftExpiredException.class)
    public ResponseEntity <Map<String, String>> handleDraftExpired(DraftExpiredException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of ("erro", ex.getMessage()));
    }

    @ExceptionHandler (EmailAlreadyInUseException.class)
    public ResponseEntity <Map<String, String>> handleEmailAlreadyInUse(EmailAlreadyInUseException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of ("erro", ex.getMessage()));
    }

    @ExceptionHandler (InvalidPaymentDayException.class)
    public ResponseEntity <Map<String,String>> handleInvalidPaymentDay (InvalidPaymentDayException ex) {
        return ResponseEntity.badRequest().body(Map.of ("erro", ex.getMessage()));
    }

    @ExceptionHandler (IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleNotFound (IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler (IllegalStateException.class)
    public  ResponseEntity<Map<String, String>> handleConlict (IllegalStateException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity <Map<String, String>> handleValidation (MethodArgumentNotValidException ex){
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(erro -> erros.put(erro.getField(), erro.getDefaultMessage()));
        return ResponseEntity.badRequest().body(erros);
    }
}
