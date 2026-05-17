package com.nutritrack.controller;

import com.nutritrack.dto.response.ApiResponse;
import com.nutritrack.dto.user.PatientProfileDTO;
import com.nutritrack.service.interfaces.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<PatientProfileDTO>> getProfile() {

        return ResponseEntity.ok(
                ApiResponse.<PatientProfileDTO>builder()
                        .success(true)
                        .message("Patient profile fetched successfully")
                        .data(patientService.getProfile())
                        .build()
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<PatientProfileDTO>> updateProfile(
            @RequestBody PatientProfileDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponse.<PatientProfileDTO>builder()
                        .success(true)
                        .message("Patient profile updated successfully")
                        .data(patientService.updateProfile(dto))
                        .build()
        );
    }
}
