package com.personaltrainer.accountdraft.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
        String password
) {
}
