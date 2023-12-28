package org.sopt.presentation.auth.dto.request;

public abstract class AuthRequest {

    public record InternalAuthRequest(
        String email,
        String password
    ) {
    }
    public record InternalSignupRequest(
            String username,
            String password,
            String email
    ) {
    }

}

