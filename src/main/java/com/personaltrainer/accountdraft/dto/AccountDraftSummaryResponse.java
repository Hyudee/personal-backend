package com.personaltrainer.accountdraft.dto;

import com.personaltrainer.accountdraft.AccountDraft;
import com.personaltrainer.accountdraft.AccountDraftStatus;
import com.personaltrainer.billing.plan.dto.PlanResponse;

import java.time.LocalDateTime;

public record AccountDraftSummaryResponse(
        Long id,
        String name,
        String email,
        AccountDraftStatus status,
        PlanResponse plan,
        Integer paymentDay,
        String pixKey,
        String rejectionReason,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {
    public static AccountDraftSummaryResponse from(AccountDraft draft) {
        return new AccountDraftSummaryResponse(
                draft.getId(),
                draft.getName(),
                draft.getEmail(),
                draft.getStatus(),
                draft.getPlan() != null ? PlanResponse.from(draft.getPlan()) : null,
                draft.getPaymentDay(),
                draft.getPixKey() != null ? draft.getPixKey().getKeyValue() : null,
                draft.getRejectionReason(),
                draft.getExpiresAt(),
                draft.getCreatedAt()
        );
    }
}
