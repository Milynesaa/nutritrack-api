package com.nutritrack.repository;

import com.nutritrack.entity.AIChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIChatHistoryRepository extends JpaRepository<AIChatHistory, Long> {
}