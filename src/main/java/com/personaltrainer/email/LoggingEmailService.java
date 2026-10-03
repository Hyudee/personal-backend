package com.personaltrainer.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoggingEmailService implements EmailService{

    @Override
    public void sendAccountApproved (String to, String name){
        log.info("[EMAIL] para {} | Olá, {}! Seu cadastro na plataforma Pedro Personal Trainer foi aprovado. Entre para completar seus dados e iniciar o treino!", to, name);
    }
}
