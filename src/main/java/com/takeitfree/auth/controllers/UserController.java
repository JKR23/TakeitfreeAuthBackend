package com.takeitfree.auth.controllers;

import com.takeitfree.auth.config.utils.SecurityUtils;
import com.takeitfree.auth.request.EmailAndPasswordRequest;
import com.takeitfree.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody EmailAndPasswordRequest emailAndPasswordRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return errorsValidation(bindingResult);
        }

        return ResponseEntity.status(200).body(this.userService.updatePassword(emailAndPasswordRequest.getEmail(), emailAndPasswordRequest.getPassword()));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logOut (@RequestHeader("Authorization") String authHeader){
        String token = SecurityUtils.extractTokenFromHeader(authHeader);

        return ResponseEntity.status(200).body(this.userService.logout(token));
    }

    private ResponseEntity<?> errorsValidation(BindingResult bindingResult) {
        List<String> errorsMessages = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest().body(errorsMessages);
    }
}
