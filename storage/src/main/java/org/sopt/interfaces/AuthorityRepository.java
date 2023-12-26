package org.sopt.interfaces;

import org.sopt.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    public List<Authority> findAllByUserId(final Long userId);
}
