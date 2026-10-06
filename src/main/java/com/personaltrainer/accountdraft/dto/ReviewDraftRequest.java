package com.personaltrainer.accountdraft.dto;

import jakarta.validation.constraints.NotNull;

public record ReviewDraftRequest(
        String reason
) {
}
