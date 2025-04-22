package com.example.capstone.skincaremission.repository;

import com.example.capstone.skincaremission.domain.MissionTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionTemplateRepository extends JpaRepository<MissionTemplate, Long> {

    Optional<MissionTemplate> findByCategoryAndScore(String category, String score);
}
