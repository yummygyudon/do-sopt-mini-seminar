package org.sopt.global.Interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.common.util.RequestUtils;
import org.sopt.entity.Authority;
import org.sopt.entity.enums.Role;
import org.sopt.exception.ForbiddenException;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.error.ForbiddenError;
import org.sopt.exception.error.NotFoundError;
import org.sopt.global.Interceptor.annotation.PermissionScope;
import org.sopt.interfaces.AuthorityRepository;
import org.sopt.jwt.provider.model.AccessTokenInfo;
import org.sopt.jwt.service.JwtAuthTokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionScopeInterceptor implements HandlerInterceptor {

    private final AuthorityRepository authorityRepository;

    private final JwtAuthTokenService jwtAuthTokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        PermissionScope permissionScope = handlerMethod.getMethodAnnotation(PermissionScope.class);

        if (Objects.isNull(permissionScope)) {
            return true;
        }
        String authorizationAccessToken = RequestUtils.getAuthorizationAccessToken(request);

        AccessTokenInfo accessTokenInfo = jwtAuthTokenService.makeAccessTokenToInfo(authorizationAccessToken);
        List<Role> accessUserRoles
                = authorityRepository.findAllByUserId(accessTokenInfo.getUserId()).stream()
                .map(Authority::getRole)
                .toList();
        if (accessUserRoles.isEmpty()) {
            throw new NotFoundException(NotFoundError.USER_DOES_NOT_HAVE_ANY_ROLE);
        }
        List<Role> targetRoles = List.of(permissionScope.targetRoles());
        log.info("Target Roles : {}", targetRoles);
        boolean isValidPermission = new HashSet<>(accessUserRoles).containsAll(targetRoles);
//        boolean isValidPermission = accessUserRoles.stream()
//                .allMatch(authority -> targetRoles.contains(authority.getRole()));
        if (!isValidPermission) {
            throw new ForbiddenException(ForbiddenError.NO_PERMISSION);
        }
        return true;
    }
}
