package com.takeitfree.auth.service.impl;

import com.takeitfree.auth.dto.RoleDTO;
import com.takeitfree.auth.dto.UserPublicDTO;
import com.takeitfree.auth.models.Role;
import com.takeitfree.auth.repositories.RoleRepository;
import com.takeitfree.auth.service.RoleService;
import com.takeitfree.auth.validators.ObjectValidator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ObjectValidator objectValidator;

    @Override
    public List<RoleDTO> getRoles() {
        return RoleDTO.toDTO((List<Role>) this.roleRepository.findAll());
    }

    @Override
    public List<UserPublicDTO> getUsersByRoleName(String roleName) {

        this.objectValidator.validate(roleName);

        List<Role> roles = this.roleRepository.findByNameContainingIgnoreCase(roleName);

        return UserPublicDTO.toDTO(
                roles.stream()
                        .flatMap(role -> role.getUserList().stream())
                        .toList()
        );
    }

    @Override
    public String addRole(RoleDTO role) {

        this.objectValidator.validate(role);

        Optional<Role> optionalRole = Optional.ofNullable(this.roleRepository.findByName(role.getName()));

        if (optionalRole.isPresent()) {
            throw new EntityExistsException("Role with name " + role.getName() + " already exists");
        }

        Role roleEntity = RoleDTO.toEntity(role);
        roleEntity.setName(role.getName().toUpperCase());

        this.roleRepository.save(roleEntity);

        return "role added successfully";

    }

    @Override
    @Transactional
    public String updateRole(RoleDTO role) {

        this.objectValidator.validate(role);

        Optional<Role> optionalRole = this.roleRepository.findById(role.getId());

        if (optionalRole.isEmpty()) {
            throw new EntityNotFoundException("Role not found");
        }

        optionalRole.get().setName(role.getName().toUpperCase());

        this.roleRepository.save(RoleDTO.toEntity(role));

        return "role updated successfully";

    }

    @Override
    public String deleteRole(Long id) {
        try {
            this.objectValidator.validate(id);

            Optional<Role> optionalRole = this.roleRepository.findById(id);

            if (optionalRole.isEmpty()) {
                throw new EntityNotFoundException("Role not found");
            }

            this.roleRepository.deleteById(id);

            return "role deleted successfully";
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
