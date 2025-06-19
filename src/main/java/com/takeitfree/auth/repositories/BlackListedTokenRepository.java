package com.takeitfree.auth.repositories;

import com.takeitfree.auth.models.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, Long> {
    boolean existsByToken(String token);
}
