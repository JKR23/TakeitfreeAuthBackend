package com.takeitfree.auth.repositories;

import com.takeitfree.auth.models.Profile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUsernameContainingIgnoreCase(@NotBlank(message = "username cannot be blank") @Size(max = 25, message = "username too long") String username);
}
