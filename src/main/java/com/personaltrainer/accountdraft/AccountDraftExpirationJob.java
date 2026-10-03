package com.personaltrainer.accountdraft;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountDraftExpirationJob {

    private final AccountDraftRepository accountDraftRepository;

    @Scheduled (cron = "0 0 3 * * *")
    @Transactional
    public void expirarDraftsVencidos () {
        List <AccountDraft> vencidos = accountDraftRepository
                .findByStatusAndExpiresAtBefore(AccountDraftStatus.PENDING_PAYMENT, LocalDateTime.now());
        vencidos.forEach(draft -> draft.setStatus(AccountDraftStatus.EXPIRED));
        accountDraftRepository.saveAll(vencidos);

        if (!vencidos.isEmpty()) {
            log.info("{} draft(s) expirado(s)", vencidos.size());
        }
    }
}
