package com.example.capstone.skincaremission.repository;

import com.example.capstone.skincaremission.domain.UserMissionCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MissionCheckRepository extends JpaRepository<UserMissionCheck, Long> {

    void deleteByUserId(String userId);

    Optional<UserMissionCheck> findByUserIdAndDate(String userId, LocalDate date);

    Optional<UserMissionCheck> findTopByUserIdOrderByDateAsc(String userId);
}
