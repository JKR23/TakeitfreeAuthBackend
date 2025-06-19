package com.takeitfree.auth.service;

import com.takeitfree.auth.dto.ProfileDTO;
import com.takeitfree.auth.models.Profile;

import java.util.List;

public interface ProfileService {
    public List<ProfileDTO> getProfiles();
    public String updateProfiles(ProfileDTO profileDTO);
    public String deleteProfiles(Long id);
    public List<ProfileDTO> getProfileByName(String name);
}
