package com.nutritrack.service.impl;

import com.nutritrack.dto.user.PatientProfileDTO;
import com.nutritrack.entity.PatientProfile;
import com.nutritrack.entity.User;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.repository.PatientProfileRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.CurrentUserService;
import com.nutritrack.service.interfaces.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final UserRepository userRepository; // Keep for now, might be removed if not used elsewhere
    private final PatientProfileRepository patientProfileRepository; // Injected
    private final CurrentUserService currentUserService; // Injected

    @Override
    public PatientProfileDTO getProfile() {
        User currentUser = currentUserService.getCurrentUser();
        PatientProfile profile = patientProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        return mapToPatientProfileDTO(profile);
    }

    @Override
    public PatientProfileDTO updateProfile(PatientProfileDTO dto) {
        User currentUser = currentUserService.getCurrentUser();
        PatientProfile profile = patientProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        profile.setAge(dto.getAge());
        profile.setWeight(dto.getWeight());
        profile.setHeight(dto.getHeight());
        profile.setGoal(dto.getGoal());
        profile.setGender(dto.getGender());
        profile.setActivityLevel(dto.getActivityLevel());

        patientProfileRepository.save(profile);
        return mapToPatientProfileDTO(profile);
    }

    private PatientProfileDTO mapToPatientProfileDTO(PatientProfile profile) {
        return PatientProfileDTO.builder()
                .age(profile.getAge())
                .weight(profile.getWeight())
                .height(profile.getHeight())
                .goal(profile.getGoal())
                .gender(profile.getGender())
                .activityLevel(profile.getActivityLevel())
                .build();
    }
}