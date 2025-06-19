package com.takeitfree.auth.dto;

import com.takeitfree.auth.models.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {

    private Long id;

    @NotBlank(message = "name cannot be blank")
    @Size(max = 15, message = "name too long")
    private String name;

    public static RoleDTO toDTO(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static Role toEntity(RoleDTO roleDTO) {
        return Role.builder()
                .id(roleDTO.getId())
                .name(roleDTO.getName())
                .build();
    }

    public static List<RoleDTO> toDTO(List<Role> roles) {
        return roles.stream().map(RoleDTO::toDTO).collect(Collectors.toList());
    }

    public static List<Role> toEntity(List<RoleDTO> roles) {
        return roles.stream().map(RoleDTO::toEntity).collect(Collectors.toList());
    }
}
