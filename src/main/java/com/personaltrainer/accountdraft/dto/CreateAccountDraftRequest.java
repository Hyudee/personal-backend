package com.personaltrainer.accountdraft.dto;

import jakarta.validation.constraints.*;

public record CreateAccountDraftRequest(
        @NotBlank
        @Size (max = 255)
        String name,

        @NotBlank
        @Email
        @Size (max = 255)
        String email,

        @NotBlank
        @Size (min = 8, max = 100)
        String password,

        @NotNull
        Long planId,

        @NotNull
        @Min (1) @Max (30)
        Integer paymentDay
) {
}
