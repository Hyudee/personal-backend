package com.personaltrainer.accountdraft.dto;

import com.personaltrainer.accountdraft.AccountDraft;
import com.personaltrainer.accountdraft.AccountDraftStatus;
import com.personaltrainer.billing.plan.dto.PlanResponse;

import java.time.LocalDateTime;

public record AccountDraftResponse(
        Long id,
        String name,
        String email,
        AccountDraftStatus status,
        PlanResponse plan,
        Integer paymentDay,
        String pixKey,
        String whatsappPaymentUrl,
        String whatsappPixPaidUrl,
        LocalDateTime expiresAt,
        LocalDateTime createdAt
) {
    public static AccountDraftResponse from (AccountDraft draft, String whatsappPaymentUrl, String whatsappPixPaidUrl) {
        return new AccountDraftResponse(
                draft.getId(),
                draft.getName(),
                draft.getEmail(),
                draft.getStatus(),
                draft.getPlan() !=null ? PlanResponse.from(draft.getPlan()) : null,
                draft.getPaymentDay(),
                draft.getPixKey() != null ? draft.getPixKey().getKeyValue() : null,
                whatsappPaymentUrl,
                whatsappPixPaidUrl,
                draft.getExpiresAt(),
                draft.getCreatedAt()

        );
    }
}
