package org.sopt.interfaces;

import org.sopt.entity.AuthorizationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizationTokenRepository extends JpaRepository<AuthorizationToken, Long> {

    Optional<AuthorizationToken> findByUserId(final Long userId);

    Optional<AuthorizationToken> findByRefreshToken(final String rtk);
}
