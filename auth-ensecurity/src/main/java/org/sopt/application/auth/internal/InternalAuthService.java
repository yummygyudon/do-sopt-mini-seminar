package org.sopt.application.auth.internal;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.CustomInternalAuthUser;
import org.sopt.entity.Authority;
import org.sopt.entity.MiminarUser;
import org.sopt.entity.enums.RegisterPlatform;
import org.sopt.entity.enums.Role;
import org.sopt.exception.error.NotFoundError;
import org.sopt.interfaces.AuthorityRepository;
import org.sopt.interfaces.MiminarUserRepository;
import org.sopt.presentation.auth.dto.request.AuthRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalAuthService implements UserDetailsService {
        private final MiminarUserRepository userRepository;
        private final AuthorityRepository authorityRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                MiminarUser miminarUser = userRepository.findById(Long.parseLong(username))
                        .orElseThrow(() -> new UsernameNotFoundException(NotFoundError.USER_NOT_FOUND.getErrorMessage()));
                List<Authority> userAuthorities = authorityRepository.findAllByUserId(miminarUser.getId());
                return new CustomInternalAuthUser(miminarUser, userAuthorities);
        }

        @Transactional
        public void signup(AuthRequest.InternalSignupRequest signupRequest) {
                MiminarUser miminarUser = registerUser(signupRequest);
                registerDefaultAuthority(miminarUser.getId());
        }


        private MiminarUser registerUser(AuthRequest.InternalSignupRequest signupRequest) {
                MiminarUser newUser = MiminarUser.builder()
                        .name(signupRequest.username())
                        .password(signupRequest.password())
                        .email(signupRequest.email())
                        .platform(RegisterPlatform.INTERNAL)
                        .build();
                return userRepository.save(newUser);
        }

        private void registerDefaultAuthority(Long userId) {
                Authority newAuthority = Authority.builder()
                        .userId(userId)
                        .role(Role.MEMBER)
                        .build();
                authorityRepository.save(newAuthority);
        }
}
