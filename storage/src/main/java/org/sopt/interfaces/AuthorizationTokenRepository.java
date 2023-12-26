package org.sopt.interfaces;

import org.sopt.entity.AuthorizationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationTokenRepository extends JpaRepository<AuthorizationToken, Long> {

    public AuthorizationToken findByUserId(final Long userId);

    public AuthorizationToken findByRefreshToken(final String rtk);
}
