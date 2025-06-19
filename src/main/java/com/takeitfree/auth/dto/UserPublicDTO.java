package com.takeitfree.auth.dto;

import com.takeitfree.auth.models.Profile;
import com.takeitfree.auth.models.User;
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
public class UserPublicDTO implements Serializable {

    private Long id;

    private Profile profile;

    private List<RoleDTO> roleDTOList;

    public static UserPublicDTO toDTO(User user) {
        return UserPublicDTO.builder()
                .id(user.getId())
                .profile(user.getProfile())
                .roleDTOList(user.getRoles().stream().map(RoleDTO::toDTO).collect(Collectors.toList()))
                .build();
    }

    public static User toEntity(UserPublicDTO userPublicDTO) {
        return User.builder()
                .id(userPublicDTO.getId())
                .profile(userPublicDTO.getProfile())
                .roles(userPublicDTO.roleDTOList.stream().map(RoleDTO::toEntity).collect(Collectors.toSet()))
                .build();
    }

    public static List<UserPublicDTO> toDTO(List<User> users) {
        return users.stream().map(UserPublicDTO::toDTO).collect(Collectors.toList());
    }

    public static List<User> toEntity(List<UserPublicDTO> users) {
        return users.stream().map(UserPublicDTO::toEntity).collect(Collectors.toList());
    }
}
