package org.sopt.presentation.auth.dto.request;

public abstract class AuthRequest {

    public record InternalAuthRequest(
        String username,
        String password
    ) {
    }
    public record InternalSignupRequest(
            String username,
            String password,
            String email
    ) {
    }

    public record SocialAuthRequest(

    ) {
    }

}

