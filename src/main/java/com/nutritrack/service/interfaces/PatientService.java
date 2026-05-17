package com.nutritrack.service.interfaces;

import com.nutritrack.dto.user.PatientProfileDTO;

public interface PatientService {

    PatientProfileDTO getProfile();

    PatientProfileDTO updateProfile(PatientProfileDTO dto);
}
