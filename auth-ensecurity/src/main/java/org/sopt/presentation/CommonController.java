package org.sopt.presentation;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.CustomInternalAuthUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommonController {

    @GetMapping("/api/v1/home")
    public String home(
            @AuthenticationPrincipal CustomInternalAuthUser user) {
        return user.getEmail();
    }
}
