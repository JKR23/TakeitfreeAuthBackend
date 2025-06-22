package com.takeitfree.auth.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class PasswordResetRequest implements Serializable {

    private String token;

    private String newPassword;
}
