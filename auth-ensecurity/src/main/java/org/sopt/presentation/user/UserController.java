package org.sopt.presentation.user;

import lombok.RequiredArgsConstructor;
import org.sopt.application.user.UserService;
import org.sopt.application.user.info.UserInfo;
import org.sopt.common.model.ApiResponse;
import org.sopt.domain.CustomInternalAuthUser;
import org.sopt.entity.enums.Role;
import org.sopt.global.Interceptor.annotation.PermissionScope;
import org.sopt.presentation.user.dto.response.UserResponse;
import org.sopt.success.OkSuccess;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 접속 유저 정보 조회
    @GetMapping("/info")
    public ApiResponse<UserResponse.UserInfo> getCurrentUserInfo(
        @AuthenticationPrincipal CustomInternalAuthUser user
    ) {
        UserInfo.UserProfile userProfile = userService.getUserProfile(user.getCustomUser().getId());
        return ApiResponse
                .success(
                    OkSuccess.VIEW_USER_INFO_SUCCESS,
                    new UserResponse.UserInfo(
                            userProfile.name(),
                            userProfile.email(),
                            String.join(",", userProfile.roles()),
                            userProfile.registerPlatform()
                    )
                );
    }

    // 특정 유저 정보 확인 - Only Admin
    @GetMapping("/info/{userId}")
    @PermissionScope(
            targetRoles = {
                    Role.ADMIN, Role.MASTER
            }
    )
    public ApiResponse<UserResponse.UserInfo> getSpecificUserInfo(
        @PathVariable("userId") Long specificUserId
    ) {
        UserInfo.UserProfile userProfile = userService.getUserProfile(specificUserId);
        return ApiResponse
                .success(
                        OkSuccess.VIEW_USER_INFO_SUCCESS,
                        new UserResponse.UserInfo(
                                userProfile.name(),
                                userProfile.email(),
                                String.join(",", userProfile.roles()),
                                userProfile.registerPlatform()
                        )
                );
    }

    // 특정 유저 제거 - Only Master
    @DeleteMapping("/remove/{userId}")
    @PermissionScope(
            targetRoles = {
                    Role.MASTER
            }
    )
    public ApiResponse<Void> deleteSpecificUserRole(
            @PathVariable("userId") Long specificUserId
    ) {
        userService.deleteUser(specificUserId);
        return ApiResponse
                .success(
                        OkSuccess.VIEW_USER_INFO_SUCCESS,
                        null
                );
    }
}
