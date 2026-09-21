package com.personaltrainer.accountdraft.dto;

import com.personaltrainer.user.User;
import com.personaltrainer.user.UserRole;
import com.personaltrainer.user.UserStatus;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role,
        UserStatus status
) {
    public static UserResponse from (User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );

    }
}
