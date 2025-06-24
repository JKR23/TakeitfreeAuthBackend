package com.takeitfree.auth.controllers;

import com.takeitfree.auth.config.utils.SecurityUtils;
import com.takeitfree.auth.dto.ProfileDTO;
import com.takeitfree.auth.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/username")
    public ResponseEntity<?> getProfilesByUsername(@RequestParam("username") String username) {
        return ResponseEntity.status(200).body(this.profileService.getProfileByName(username));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDTO profile, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return errorsValidation(bindingResult);
        }

        return ResponseEntity.status(200).body(this.profileService.updateProfiles(profile));
    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<?> deleteProfile() {

        Long idUser = SecurityUtils.getCurrentUserId();

        return ResponseEntity.status(200).body(this.profileService.deleteProfiles(idUser));
    }

    @PutMapping(value = "/update-photo-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateItem(@RequestParam("image") MultipartFile imageFile) {

        try {
            return ResponseEntity
                    .status(200)
                    .body(this.profileService.updateImageProfile(imageFile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during updating photo profile: " + e.getMessage()+" "+e.getCause());
        }
    }

    private ResponseEntity<?> errorsValidation(BindingResult bindingResult) {
        List<String> errorsMessages = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest().body(errorsMessages);
    }

}
