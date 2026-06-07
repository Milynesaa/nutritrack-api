package com.nutritrack.repository;

import com.nutritrack.entity.User;
import com.nutritrack.entity.enums.Role;
import com.nutritrack.entity.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    List<User> findByRoleAndStatus(Role role, UserStatus status);
}