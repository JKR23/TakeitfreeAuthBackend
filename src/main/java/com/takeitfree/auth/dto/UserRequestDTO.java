package com.takeitfree.auth.dto;

import com.takeitfree.auth.models.Profile;
import com.takeitfree.auth.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UserRequestDTO implements Serializable {

    private Long id;

    @NotBlank(message = "email cannot be null or empty")
    @Email(message = "email format is invalid")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "email does not match required pattern"
    )
    private String email;

    @NotBlank(message = "password cannot be null or empty")
    @Size(min = 8, max = 16, message = "password too long or too short")
    private String password;

    @NotBlank(message = "username cannot be blank")
    @Size(max = 25, message = "username too long")
    private String username;

    public static UserRequestDTO toDTO(User user) {
        return UserRequestDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getProfile().getUsername())
                .build();
    }

    public static User toEntity(UserRequestDTO dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .profile(Profile.builder()
                        .username(dto.getUsername())
                        .build()
                )
                .build();
    }

    public static List<UserRequestDTO> toDTO(List<User> users) {
        return users.stream().map(UserRequestDTO::toDTO).collect(Collectors.toList());
    }

    public static List<User> toEntity(List<UserRequestDTO> usersDTo) {
        return usersDTo.stream().map(UserRequestDTO::toEntity).collect(Collectors.toList());
    }
}
