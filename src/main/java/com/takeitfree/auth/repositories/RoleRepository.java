package com.takeitfree.auth.repositories;

import com.takeitfree.auth.dto.UserPublicDTO;
import com.takeitfree.auth.models.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    List<Role> findByNameContainingIgnoreCase(@NotBlank(message = "name cannot be blank") @Size(max = 15, message = "name too long") String name);
    Role findByName(@NotBlank(message = "name cannot be blank") String name);
}
