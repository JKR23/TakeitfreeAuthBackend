package com.takeitfree.auth.controllers.admin;

import com.takeitfree.auth.request.AttributeRoleRequest;
import com.takeitfree.auth.request.EmailRequest;
import com.takeitfree.auth.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Admin User Management", description = "APIs for admin-only user operations")
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(200).body(this.userService.getAllUsers());
    }

    @GetMapping("/email")
    public ResponseEntity<?> getUserByEmail(@Valid @RequestBody EmailRequest emailRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return errorsValidation(bindingResult);
        }

        return ResponseEntity.status(200).body(this.userService.getUserByEmail(emailRequest.getEmail()));
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<?> delete(@Valid @PathVariable Long id) {
        return ResponseEntity.status(200).body(this.userService.deleteUser(id));
    }

    @GetMapping("/username")
    public ResponseEntity<?> getUserByUsername(@RequestParam("username") String username) {
        return ResponseEntity.status(200).body(this.userService.findByUsername(username));
    }

    @PutMapping("/attribute-role")
    public ResponseEntity<?> updateAttributeRole(@Valid @RequestBody AttributeRoleRequest attributeRoleRequest,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return errorsValidation(bindingResult);
        }

        return ResponseEntity
                .status(200)
                .body(this.userService.attributeRoleToUser(
                        attributeRoleRequest.getIdUser(),
                        attributeRoleRequest.getIdRole())
                );
    }

    private ResponseEntity<?> errorsValidation(BindingResult bindingResult) {
        List<String> errorsMessages = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest().body(errorsMessages);
    }
}
