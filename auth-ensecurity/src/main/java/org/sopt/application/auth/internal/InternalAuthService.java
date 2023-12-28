package org.sopt.application.auth.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.sopt.common.model.AuthTokenResponse;
import org.sopt.domain.CustomInternalAuthUser;
import org.sopt.entity.Authority;
import org.sopt.entity.MiminarUser;
import org.sopt.entity.enums.RegisterPlatform;
import org.sopt.entity.enums.Role;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.error.NotFoundError;
import org.sopt.interfaces.AuthorityRepository;
import org.sopt.interfaces.MiminarUserRepository;
import org.sopt.jwt.provider.model.AccessTokenInfo;
import org.sopt.jwt.service.JwtAuthTokenService;
import org.sopt.presentation.auth.dto.request.AuthRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        @Transactional(readOnly = true)
        public AccessTokenInfo login(AuthRequest.InternalAuthRequest authRequest){
                MiminarUser targetUser = userRepository.findByEmailAndPasswordAndPlatform(
                        authRequest.email(), authRequest.password(), RegisterPlatform.INTERNAL)
                        .orElseThrow(() -> new NotFoundException(NotFoundError.USER_NOT_FOUND));
                return AccessTokenInfo.of(targetUser.getId());
        }



        @Transactional
        public boolean signup(AuthRequest.InternalSignupRequest signupRequest) {
                Optional<MiminarUser> targetUser = userRepository.findByEmailAndPasswordAndPlatform(
                        signupRequest.email(), signupRequest.password(), RegisterPlatform.INTERNAL);
                if (targetUser.isPresent()) {
                        return false;
                }
                MiminarUser miminarUser = registerUser(signupRequest);
                registerDefaultAuthority(miminarUser.getId());
                return true;
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
