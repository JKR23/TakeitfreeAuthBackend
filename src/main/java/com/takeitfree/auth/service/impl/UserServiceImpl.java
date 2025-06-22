package com.takeitfree.auth.service.impl;

import com.takeitfree.auth.config.utils.JwtUtils;
import com.takeitfree.auth.dto.AuthenticationResponse;
import com.takeitfree.auth.dto.UserPublicDTO;
import com.takeitfree.auth.dto.UserRequestDTO;
import com.takeitfree.auth.models.Role;
import com.takeitfree.auth.models.User;
import com.takeitfree.auth.repositories.RoleRepository;
import com.takeitfree.auth.repositories.UserRepository;
import com.takeitfree.auth.request.EmailAndPasswordRequest;
import com.takeitfree.auth.service.BlackListedTokenService;
import com.takeitfree.auth.service.UserService;
import com.takeitfree.auth.validators.ObjectValidator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectValidator objectValidator;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BlackListedTokenService blackListedTokenService;

    @Override
    public List<UserPublicDTO> getAllUsers() {
        return UserPublicDTO.toDTO((List<User>) this.userRepository.findAll());
    }

    @Override
    public UserPublicDTO getUserByEmail(String email) {
        try {

            this.objectValidator.validate(email);

            Optional<User> user = Optional.ofNullable(this.userRepository.findByEmail(email));

            if (user.isEmpty()) {
                throw new EntityNotFoundException("User not found");
            }

            return UserPublicDTO.toDTO(user.get());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public String addUser(UserRequestDTO user) {

        this.objectValidator.validate(user);

        Optional<User> userToCreate = Optional.ofNullable(this.userRepository.findByEmail(user.getEmail()));

        if (userToCreate.isPresent()) {
            throw new EntityExistsException("Email already exists");
        }

        User userToSave = UserRequestDTO.toEntity(user);

        String urlImage = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";

        userToSave.getProfile().setActive(true);
        userToSave.getProfile().setUrlImage(urlImage);

        //Crypt password
        userToSave.setPassword(this.passwordEncoder.encode(user.getPassword()));

        this.userRepository.save(attributeRoleUserInSignUp(userToSave));

        return "User added successfully";
    }

    @Override
    public AuthenticationResponse loginUser(EmailAndPasswordRequest emailAndPasswordRequest) {

        this.objectValidator.validate(emailAndPasswordRequest);

        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        emailAndPasswordRequest.getEmail(), emailAndPasswordRequest.getPassword()
                )
        );

        //Generate token
        final UserDetails userDetails = this.userRepository.findByEmail(emailAndPasswordRequest.getEmail());
        final Long IdUser = this.userRepository.findByEmail(emailAndPasswordRequest.getEmail()).getId();
        final String token = this.jwtUtils.generateToken(userDetails, IdUser);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public String logout(String token) {

        this.blackListedTokenService.addBlackListedToken(token);

        return "Logout successful";
    }

    @Override
    public String deleteUser(Long id) {

        this.objectValidator.validate(id);

        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User doesn't exist with this id");
        }

        this.userRepository.delete(optionalUser.get());

        return "User deleted successfully";
    }

    @Override
    public List<UserPublicDTO> findByUsername(String username) {

        this.objectValidator.validate(username);

        return UserPublicDTO.toDTO(this.userRepository.findByUsernameContainingIgnoreCase(username));
    }

    @Override
    public String attributeRoleToUser(Long id, Long idRole) {
        this.objectValidator.validate(id);
        this.objectValidator.validate(idRole);

        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User doesn't exist with this id");
        }

        Optional<Role> optionalRole = this.roleRepository.findById(idRole);

        if (optionalRole.isEmpty()) {
            throw new EntityNotFoundException("Role doesn't exist with this id");
        }

        if (optionalUser.get().getRoles() == null || optionalUser.get().getRoles().isEmpty()){
            optionalUser.get().setRoles(new HashSet<>());
        }

        optionalUser.get().getRoles().add(optionalRole.get());

        this.userRepository.save(optionalUser.get());

        return "Role "+optionalRole.get().getName()+" attribute successfully to "
                +optionalUser.get().getProfile().getUsername();

    }

    private User attributeRoleUserInSignUp(User user) {

        Role role = roleRepository.findByName("ROLE_USER");

        if (user.getRoles() == null || user.getRoles().isEmpty()){
            user.setRoles(new HashSet<>());
        }

        user.getRoles().add(role);

        return user;
    }
}
