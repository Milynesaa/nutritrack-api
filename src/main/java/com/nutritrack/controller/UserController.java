package com.nutritrack.controller;

import com.nutritrack.dto.response.ApiResponse;
import com.nutritrack.dto.user.UserResponse;
import com.nutritrack.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User profile fetched successfully")
                        .data(userService.getCurrentUser())
                        .build()
        );
    }
}