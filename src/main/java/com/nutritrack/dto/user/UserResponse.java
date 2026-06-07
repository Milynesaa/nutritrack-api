package com.nutritrack.dto.user;

import com.nutritrack.entity.enums.Role;
import com.nutritrack.entity.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Role role;

    private UserStatus status;

    private LocalDateTime createdAt;
}
