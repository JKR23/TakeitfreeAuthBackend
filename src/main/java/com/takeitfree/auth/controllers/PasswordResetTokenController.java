package com.takeitfree.auth.controllers;

import com.takeitfree.auth.exceptions.TokenExpiredException;
import com.takeitfree.auth.request.EmailRequest;
import com.takeitfree.auth.request.PasswordResetRequest;
import com.takeitfree.auth.service.PasswordResetTokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("password-reset")
@RequiredArgsConstructor
public class PasswordResetTokenController {

    private final PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/obtain-resetLink")
    public ResponseEntity<?> obtainResetLink(@Valid @RequestBody EmailRequest emailRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return errorsValidation(bindingResult);
        }

        return ResponseEntity.status(200).body(this.passwordResetTokenService.sendLinkResetPassword(emailRequest.getEmail()));
    }

    @PutMapping("/set-new-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return errorsValidation(bindingResult);
        }

        return ResponseEntity.status(200).body(this.passwordResetTokenService.resetPassword(passwordResetRequest.getToken(), passwordResetRequest.getNewPassword()));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> checkTokenValidity(@RequestParam("token") String token) {
        passwordResetTokenService.checkTokenValidity(token);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> errorsValidation(BindingResult bindingResult) {
        List<String> errorsMessages = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest().body(errorsMessages);
    }

}
