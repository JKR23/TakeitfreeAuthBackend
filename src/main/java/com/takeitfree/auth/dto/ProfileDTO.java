package com.takeitfree.auth.dto;

import com.takeitfree.auth.models.Profile;
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
public class ProfileDTO implements Serializable {
    private Long id;

    @NotBlank(message = "username cannot be blank")
    @Size(max = 25, message = "username too long")
    private String username;

    @Size(max = 500, message = "path image too long")
    private String urlImage;

    public static ProfileDTO toDTO(Profile profile) {
        return ProfileDTO.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .urlImage(profile.getUrlImage())
                .build();
    }

    public static Profile toEntity(ProfileDTO profile) {
        return Profile.builder()
                .id(profile.getId())
                .username(profile.getUsername())
                .urlImage(profile.getUrlImage())
                .build();
    }

    public static List<ProfileDTO> toDTO(List<Profile> profiles) {
        return profiles.stream().map(ProfileDTO::toDTO).collect(Collectors.toList());
    }

    public static List<Profile> toEntity(List<ProfileDTO> profiles) {
        return profiles.stream().map(ProfileDTO::toEntity).collect(Collectors.toList());
    }
}
