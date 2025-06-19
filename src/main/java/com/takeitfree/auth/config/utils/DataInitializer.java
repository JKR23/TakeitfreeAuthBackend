package com.takeitfree.auth.config.utils;

import com.takeitfree.auth.dto.UserRequestDTO;
import com.takeitfree.auth.models.Role;
import com.takeitfree.auth.models.User;
import com.takeitfree.auth.repositories.RoleRepository;
import com.takeitfree.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create roles if none role present
        if (roleRepository.count() == 0) {

            //Create roles
            roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
            roleRepository.save(Role.builder().name("ROLE_USER").build());
        }

        String emailAdmin = "admin@email.com";

        Optional<User> optionalUserList = Optional.ofNullable(userRepository.findByEmail(emailAdmin));

        // Création administrator
        if (optionalUserList.isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            Role userRole = roleRepository.findByName("ROLE_USER");

            if (adminRole == null) {
                throw  new RuntimeException("ROLE_ADMIN not found");
            }

            if (userRole == null) {
                throw  new RuntimeException("ROLE_USER not found");
            }

            Set<Role> roleHashSet = new HashSet<>();
            roleHashSet.add(adminRole);
            roleHashSet.add(userRole);

            //Build UserRequestDTO for admin
            UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                    .username("admin")
                    .email("admin@email.com")
                    .password(passwordEncoder.encode("admin"))
                    .build();

            //Transform to an Entity
            User userAdmin = UserRequestDTO.toEntity(userRequestDTO);

            //Attribute role to Admin
            userAdmin.setRoles(roleHashSet);

            // Activate user
            userAdmin.getProfile().setActive(true);

            //Fix profile image
            String urlImage = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png";

            userAdmin.getProfile().setUrlImage(urlImage);

            userRepository.save(userAdmin);
        }
    }
}

