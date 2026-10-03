package com.personaltrainer.accountdraft;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountDraftRepository extends JpaRepository<AccountDraft, Long>{

    // Um email pode ter varios drafts (rejeitado ou expirado), mas somente um pendente por vez
    Optional<AccountDraft> findByEmailAndStatus (String email, AccountDraftStatus status);

    boolean existsByEmailAndStatus(String email, AccountDraftStatus status);

    List<AccountDraft> findByStatusAndExpiresAtBefore (AccountDraftStatus status, LocalDateTime dateTime);
}
