package com.takeitfree.auth.controllers;

import com.takeitfree.auth.dto.UserRequestDTO;
import com.takeitfree.auth.request.EmailAndPasswordRequest;
import com.takeitfree.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRequestDTO users, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return errorsValidation(bindingResult);
        }

        return ResponseEntity.status(200).body(this.userService.addUser(users));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody EmailAndPasswordRequest emailAndPasswordRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return errorsValidation(bindingResult);
        }

        return ResponseEntity.status(200).body(this.userService.loginUser(emailAndPasswordRequest));
    }

    private ResponseEntity<?> errorsValidation(BindingResult bindingResult) {
        List<String> errorsMessages = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest().body(errorsMessages);
    }
}
