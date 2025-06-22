package com.takeitfree.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class PasswordResetRequest implements Serializable {

    @NotNull(message = "error with token")
    @NotBlank(message = "error with token")
    private String token;

    @NotNull(message = "new password shouldn't be null")
    @NotBlank(message = "new password shouldn't be empty")
    private String newPassword;
}
