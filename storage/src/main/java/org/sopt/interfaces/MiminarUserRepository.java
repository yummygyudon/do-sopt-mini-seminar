package org.sopt.interfaces;

import org.sopt.entity.MiminarUser;
import org.sopt.entity.enums.RegisterPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MiminarUserRepository extends JpaRepository<MiminarUser, Long> {
    Optional<MiminarUser> findByEmailAndPasswordAndPlatform(String email, String password, RegisterPlatform platform);
}
