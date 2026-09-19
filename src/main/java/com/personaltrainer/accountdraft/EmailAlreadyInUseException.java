package com.personaltrainer.accountdraft;

public class EmailAlreadyInUseException extends RuntimeException{
    public EmailAlreadyInUseException(String email) {
        super ("Este Email já está vinculado à uma conta:" + email);
    }
}
