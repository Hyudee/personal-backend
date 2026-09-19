package com.personaltrainer.accountdraft;

import com.personaltrainer.user.User;
import com.personaltrainer.user.UserRepository;
import com.personaltrainer.user.UserRole;
import com.personaltrainer.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountDraftService {

    private static final long EXPIRATION_DAYS = 30;

    private final AccountDraftRepository accountDraftRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AccountDraft criarDraft(String name, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException(email);
        }

        if (accountDraftRepository.existsByEmailAndStatus(email, AccountDraftStatus.PENDING_PAYMENT)) {
            throw new EmailAlreadyInUseException(email);
        }

        AccountDraft draft = AccountDraft.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .status(AccountDraftStatus.PENDING_PAYMENT)
                .expiresAt(LocalDateTime.now().plusDays(EXPIRATION_DAYS))
                .build();

        return accountDraftRepository.save(draft);
    }

    @Transactional
    public User aprovar(Long draftId, Long reviewerId) {
        AccountDraft draft = getPendingDraft(draftId);
        User reviewer = getReviewer(reviewerId);

        User user = User.builder()
                .name(draft.getName())
                .email(draft.getEmail())
                .password(draft.getPassword())
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(user);

        draft.setStatus(AccountDraftStatus.APPROVED);
        draft.setReviewedAt(LocalDateTime.now());
        draft.setReviewer(reviewer);
        accountDraftRepository.save(draft);

        return user;
    }

    @Transactional
    public void rejeitar(Long draftId, Long reviewerId, String reason) {
        AccountDraft draft = getPendingDraft(draftId);
        User reviewer = getReviewer(reviewerId);

        draft.setStatus(AccountDraftStatus.REJECTED);
        draft.setReviewedAt(LocalDateTime.now());
        draft.setReviewer(reviewer);
        draft.setRejectionReason(reason);

        accountDraftRepository.save(draft);
    }

    private AccountDraft getPendingDraft(Long draftId) {
        AccountDraft draft = accountDraftRepository.findById(draftId)
                .orElseThrow(() -> new IllegalArgumentException("Draft não encontrado: " + draftId));

        if (draft.getStatus() != AccountDraftStatus.PENDING_PAYMENT) {
            throw new IllegalStateException(
                    "Draft não está pendente (status atual: " + draft.getStatus() + ")");
        }

        if (draft.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Draft expirado em " + draft.getExpiresAt());
        }

        return draft;
    }

    private User getReviewer(Long reviewerId) {
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Revisor não encontrado: " + reviewerId));

        if (reviewer.getRole() != UserRole.PERSONAL) {
            throw new IllegalStateException("Apenas um personal pode revisar um draft");
        }

        return reviewer;
    }
}