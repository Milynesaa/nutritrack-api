package com.nutritrack.repository;

import com.nutritrack.entity.AIChatHistory;
import com.nutritrack.entity.User; // Import User entity
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIChatHistoryRepository extends JpaRepository<AIChatHistory, Long> {
    List<AIChatHistory> findByUser(User user);
}