package com.personaltrainer.auth;

public class PendingApprovalException extends RuntimeException {
    public PendingApprovalException() {
        super("Seu cadastro ainda está aguardando aprovação do personal.");
    }
}
