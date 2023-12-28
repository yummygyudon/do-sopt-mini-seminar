package org.sopt.presentation.user.dto.request;

public abstract class UserRequest {
    public record RevokeRoleRequest(
            String toBeRole
    ) {
    }
}
