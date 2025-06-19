package com.takeitfree.auth.service;

import com.takeitfree.auth.dto.RoleDTO;
import com.takeitfree.auth.dto.UserPublicDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getRoles();
    List<UserPublicDTO> getUsersByRoleName(String roleName);
    String addRole(RoleDTO role);
    String updateRole(RoleDTO role);
    String deleteRole(Long id);
}
