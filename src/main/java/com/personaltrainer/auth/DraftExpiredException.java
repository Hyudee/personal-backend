package com.personaltrainer.auth;

public class DraftExpiredException extends RuntimeException{
    public DraftExpiredException() {
        super("Seu cadastro expirou. Faça um novo cadastro para continuar.");
    }
}
