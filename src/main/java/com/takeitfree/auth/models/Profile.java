package com.takeitfree.auth.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "username cannot be blank")
    @Size(max = 25, message = "username too long")
    private String username;

    @Size(max = 500, message = "path image too long")
    private String urlImage;

    private boolean active;
}
