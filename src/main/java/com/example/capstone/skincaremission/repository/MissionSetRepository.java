package com.example.capstone.skincaremission.repository;

import com.example.capstone.skincaremission.domain.UserMissionSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionSetRepository extends JpaRepository<UserMissionSet, Long> {

    void deleteByUserId(String userId);

    Optional<UserMissionSet> findByUserId(String userId);
}
