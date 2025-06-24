package com.takeitfree.auth.service.impl;

import com.takeitfree.auth.config.utils.SecurityUtils;
import com.takeitfree.auth.dto.ProfileDTO;
import com.takeitfree.auth.exceptions.ImageProcessingException;
import com.takeitfree.auth.models.Profile;
import com.takeitfree.auth.models.User;
import com.takeitfree.auth.repositories.ProfileRepository;
import com.takeitfree.auth.repositories.UserRepository;
import com.takeitfree.auth.service.ProfileService;
import com.takeitfree.auth.validators.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ObjectValidator objectValidator;
    private final AzureBlobStorageServiceImpl azureBlobStorageService;

    @Override
    public List<ProfileDTO> getProfiles() {
        return ProfileDTO.toDTO(this.profileRepository.findAll());
    }

    @Override
    public String updateProfiles(ProfileDTO profileDTO) {
        try {
            this.objectValidator.validate(profileDTO);

            Long id = SecurityUtils.getCurrentUserId();

            Optional<User> optionalUser = this.userRepository.findById(id);

            if (optionalUser.isEmpty()) {
                throw new EntityNotFoundException("Motherfucker stop hacking");
            }

            Optional<Profile> updatedProfile = this.profileRepository.findById(
                    optionalUser.get().getProfile().getId()
            );

            if (updatedProfile.isEmpty()) {
                throw new EntityNotFoundException("Profile not found, are you an alien ?");
            }

            this.profileRepository.save(updateInfoProfile(updatedProfile.get(), profileDTO));

            return "Your profile has been updated";
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public String deleteProfiles(Long id) {
        try {
            this.objectValidator.validate(id);

            Optional<Profile> updatedProfile = this.profileRepository.findById(id);

            if (updatedProfile.isEmpty()) {
                throw new EntityNotFoundException("Profile not found");
            }

            updatedProfile.get().setActive(false);

            this.profileRepository.save(updatedProfile.get());

            return "Profile deleted";

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ProfileDTO> getProfileByName(String username) {

        return ProfileDTO.toDTO(this.profileRepository.findByUsernameContainingIgnoreCase(username).stream()
                .filter(Profile::isActive)
                .collect(Collectors.toList()));
    }

    @Override
    public String updateImageProfile(MultipartFile file) {

        Long idCurrentUser = SecurityUtils.getCurrentUserId();

        Optional<User> userOptional = this.userRepository.findById(idCurrentUser);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Please Login");
        }

        Optional<Profile> profileOptional = profileRepository.findById(userOptional.get().getProfile().getId());

        if (profileOptional.isEmpty()) {
            throw new EntityNotFoundException("Profile not found, are you an alien ?");
        }

        String urlProfile = processProfileImage(file);

        profileOptional.get().setUrlImage(urlProfile);

        this.profileRepository.save(profileOptional.get());

        return "Image profile updated successfully";
    }

    private Profile updateInfoProfile(Profile profileToReceiveInfo, ProfileDTO profileToSendInfo) {

        profileToReceiveInfo.setUsername(profileToSendInfo.getUsername());
        profileToReceiveInfo.setUrlImage(profileToSendInfo.getUrlImage());

        return profileToReceiveInfo;
    }

    private String processProfileImage(MultipartFile imageUrl) {
        try {
            return azureBlobStorageService.uploadFile(imageUrl);
        } catch (IOException e) {
            //log.error("Failed to upload image to Cloudinary", e);
            throw new ImageProcessingException("Failed to process image", e);
        }
    }
}
