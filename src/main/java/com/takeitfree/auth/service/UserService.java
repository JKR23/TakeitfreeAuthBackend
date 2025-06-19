package com.takeitfree.auth.service;

import com.takeitfree.auth.dto.AuthenticationResponse;
import com.takeitfree.auth.dto.UserPublicDTO;
import com.takeitfree.auth.dto.UserRequestDTO;
import com.takeitfree.auth.request.EmailAndPasswordRequest;

import java.util.List;

public interface UserService {

    public List<UserPublicDTO> getAllUsers();
    public UserPublicDTO getUserByEmail(String email);
    public String addUser(UserRequestDTO user);
    public AuthenticationResponse loginUser(EmailAndPasswordRequest emailAndPasswordRequest);
    public String logout(String token);
    public String updatePassword(String email, String newPassword);
    public String deleteUser(Long id);
    List<UserPublicDTO> findByUsername(String username);
    public String attributeRoleToUser(Long id, Long idRole);

}
