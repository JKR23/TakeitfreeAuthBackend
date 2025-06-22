package com.takeitfree.auth.repositories;

import com.takeitfree.auth.models.PasswordResetToken;
import com.takeitfree.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByEmail(String email);
}
