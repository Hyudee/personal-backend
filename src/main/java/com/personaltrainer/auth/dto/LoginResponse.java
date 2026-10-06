package com.personaltrainer.auth.dto;

import com.personaltrainer.user.UserRole;

public record LoginResponse(
        String token,
        Long userId,
        String name,
        UserRole role,
        boolean profileCompleted
) {
}
