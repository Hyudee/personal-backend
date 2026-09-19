package com.personaltrainer.accountdraft;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountDraftRepository extends JpaRepository<AccountDraft, Long>{

    Optional<AccountDraft> findByEmail (String email);

    boolean existsByEmailAndStatus(String email, AccountDraftStatus status);


    List<AccountDraft> findByStatusAndExpiresAtBefore (AccountDraftStatus status, LocalDateTime dateTime);
}
