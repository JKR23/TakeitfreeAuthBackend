package com.takeitfree.auth.controllers.admin;

import com.takeitfree.auth.dto.RoleDTO;
import com.takeitfree.auth.service.RoleService;
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
@RequestMapping("admin/role")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Admin User Management", description = "APIs for admin-only user operations")
public class AdminRoleController {

    private final RoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.status(200).body(this.roleService.getRoles());
    }

    @GetMapping("/name")
    public ResponseEntity<?> getUsersByRoleName(@RequestParam String name) {
        return ResponseEntity.status(200).body(this.roleService.getUsersByRoleName(name));
    }

    @PostMapping(path="/create")
    public ResponseEntity<?> addRole(@Valid @RequestBody RoleDTO role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorsValidation(bindingResult);
        }

        return ResponseEntity.status(200).body(this.roleService.addRole(role));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateRole(@Valid @RequestBody RoleDTO role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorsValidation(bindingResult);
        }

        return ResponseEntity.status(200).body(this.roleService.updateRole(role));
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        return ResponseEntity.status(200).body(this.roleService.deleteRole(id));
    }
    
    private ResponseEntity<?> errorsValidation(BindingResult bindingResult) {

        List<String> errorsMessages = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest().body(errorsMessages);
    }
}
