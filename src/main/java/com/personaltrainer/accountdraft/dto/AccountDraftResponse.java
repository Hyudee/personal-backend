package com.personaltrainer.accountdraft.dto;

import com.personaltrainer.accountdraft.AccountDraft;
import com.personaltrainer.accountdraft.AccountDraftStatus;

import java.time.LocalDateTime;

public record AccountDraftResponse(
        Long id,
        String name,
        String email,
        AccountDraftStatus status,
        LocalDateTime createdAt
) {
    public static AccountDraftResponse from (AccountDraft draft) {
        return new AccountDraftResponse(
                draft.getId(),
                draft.getName(),
                draft.getEmail(),
                draft.getStatus(),
                draft.getCreatedAt()

        );
    }
}
