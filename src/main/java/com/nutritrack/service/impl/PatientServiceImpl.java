package com.nutritrack.service.impl;

import com.nutritrack.dto.user.PatientProfileDTO;
import com.nutritrack.entity.PatientProfile;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.interfaces.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final UserRepository userRepository;

    @Override
    public PatientProfileDTO getProfile() {
        return null;
    }

    @Override
    public PatientProfileDTO updateProfile(PatientProfileDTO dto) {
        return dto;
    }
}
