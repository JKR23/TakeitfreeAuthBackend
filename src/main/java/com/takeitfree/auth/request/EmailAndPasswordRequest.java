package com.takeitfree.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class EmailAndPasswordRequest implements Serializable {
    @NotBlank(message = "username cannot be null or empty")
    @Email(message = "email invalid")
    private String email;

    @NotBlank(message = "password cannot be null or empty")
    private String password;
}
