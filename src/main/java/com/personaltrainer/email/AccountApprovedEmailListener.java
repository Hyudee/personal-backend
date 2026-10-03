package com.personaltrainer.email;

import com.personaltrainer.accountdraft.AccountApprovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountApprovedEmailListener {

    private final EmailService emailService;

    @TransactionalEventListener
    public void onAccountApproved (AccountApprovedEvent event){
        try {
            emailService.sendAccountApproved(event.email(), event.name());
        } catch (Exception e) {
            log.error("Falha ao enviar o email de aprovação para {}", event.email(), e);
        }
    }
}
