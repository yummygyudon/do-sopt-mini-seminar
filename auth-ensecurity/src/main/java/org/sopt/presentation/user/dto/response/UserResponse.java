package org.sopt.presentation.user.dto.response;

public abstract class UserResponse {
    public record UserInfo(
        String name,
        String email,
        String role,
        String platform
    ) {
    }
    public record UserRoleStatus(
        String role
    ) {
    }
}

