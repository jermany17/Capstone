package com.example.capstone.aidisease.repository;

import com.example.capstone.aidisease.domain.DiseaseTreatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseTreatmentRepository extends JpaRepository<DiseaseTreatment, Long> {
    Optional<DiseaseTreatment> findByDisease(String disease);
}
