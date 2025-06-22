package com.takeitfree.auth.service;

import com.takeitfree.auth.dto.ResponseLinkForResetPassword;

public interface PasswordResetTokenService {
    public ResponseLinkForResetPassword sendLinkResetPassword(String email);

    public String resetPassword(String token, String newPassword);
}
