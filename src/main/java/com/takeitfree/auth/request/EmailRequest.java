package com.takeitfree.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class EmailRequest implements Serializable {

    @NotBlank(message = "email cannot be null or empty")
    @jakarta.validation.constraints.Email(message = "email format is invalid")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "email does not match required pattern"
    )
    private String email;
}
