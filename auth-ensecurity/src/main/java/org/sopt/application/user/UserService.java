package org.sopt.application.user;

import lombok.RequiredArgsConstructor;
import org.sopt.application.user.info.UserInfo;
import org.sopt.entity.Authority;
import org.sopt.entity.MiminarUser;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.error.NotFoundError;
import org.sopt.interfaces.AuthorityRepository;
import org.sopt.interfaces.MiminarUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MiminarUserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserInfo.UserProfile getUserProfile(Long userId) {
        MiminarUser miminarUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundError.USER_NOT_FOUND));
        List<String> allAuthorities = authorityRepository.findAllByUserId(userId)
                .stream()
                .map(authority -> authority.getRole().name())
                .toList();
        return new UserInfo.UserProfile(
                miminarUser.getName(),
                miminarUser.getEmail(),
                allAuthorities,
                miminarUser.getPlatform().name()
        );
    }

    public void deleteUser(Long userId) {
        MiminarUser miminarUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundError.USER_NOT_FOUND));
        List<Authority> allAuthorities = authorityRepository.findAllByUserId(userId);
        userRepository.delete(miminarUser);
        authorityRepository.deleteAll(allAuthorities);
    }
}
