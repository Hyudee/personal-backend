package com.personaltrainer.auth;

public class DraftRejectedException extends RuntimeException{
    public DraftRejectedException(String reason) {
        super("Seu cadastro foi rejeitado" + (reason != null && !reason.isBlank() ? ": " + reason : "."));
    }
}
