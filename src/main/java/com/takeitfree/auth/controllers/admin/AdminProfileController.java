package com.takeitfree.auth.controllers.admin;

import com.takeitfree.auth.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Admin User Management", description = "APIs for admin-only user operations")
public class AdminProfileController {

    private final ProfileService profileService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllProfiles() {
        return ResponseEntity
                .status(200)
                .body(this.profileService.getProfiles());
    }

}
