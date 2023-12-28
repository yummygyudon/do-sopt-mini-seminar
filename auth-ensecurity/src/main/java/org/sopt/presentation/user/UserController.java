package org.sopt.presentation.user;

import lombok.RequiredArgsConstructor;
import org.sopt.common.model.ApiResponse;
import org.sopt.entity.MiminarUser;
import org.sopt.presentation.user.dto.request.UserRequest;
import org.sopt.presentation.user.dto.request.UserRequest.RevokeRoleRequest;
import org.sopt.presentation.user.dto.response.UserResponse;
import org.sopt.success.OkSuccess;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    // 접속 유저 정보 조회
    @GetMapping("/info/me")
    public ApiResponse<UserResponse.UserInfo> getCurrentUserInfo(
        @AuthenticationPrincipal MiminarUser user
    ) {

        return ApiResponse
                .success(
                    OkSuccess.VIEW_USER_INFO_SUCCESS,
                    new UserResponse.UserInfo()
                );
    }

    // 특정 유저 정보 확인 - Only Admin
    @GetMapping("/info/{userId}")
    public ApiResponse<UserResponse.UserInfo> getSpecificUserInfo(
        @AuthenticationPrincipal MiminarUser user,
        @PathVariable("userId") Long specificUserId
    ) {
        return ApiResponse
                .success(
                    OkSuccess.VIEW_USER_INFO_SUCCESS,
                    new UserResponse.UserInfo()
                );
    }

    // 특정 유저 권한 변경 - Only Admin & Master
    // Admin -> Member : Master 만 가능
    @PutMapping("/revoke/{userId}")
    public ApiResponse<UserResponse.UserRoleStatus> revokeSpecificUserRole(
            @AuthenticationPrincipal MiminarUser user,
            @PathVariable("userId") Long specificUserId,
            @RequestBody RevokeRoleRequest revokeRequest
    ) {

        return ApiResponse
                .success(
                        OkSuccess.VIEW_USER_INFO_SUCCESS,
                        new UserResponse.UserRoleStatus()
                );
    }

    // 특정 유저 제거 - Only Master
    @DeleteMapping("/remove/{userId}")
    public ApiResponse<?> deleteSpecificUserRole(
            @AuthenticationPrincipal MiminarUser user,
            @PathVariable("userId") Long specificUserId
    ) {
        return ApiResponse
                .success(
                        OkSuccess.VIEW_USER_INFO_SUCCESS
                );
    }
}
