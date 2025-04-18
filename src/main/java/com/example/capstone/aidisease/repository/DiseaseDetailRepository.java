package com.example.capstone.aidisease.repository;

import com.example.capstone.aidisease.domain.DiseaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseDetailRepository extends JpaRepository<DiseaseDetail, Long> {
    Optional<DiseaseDetail> findByDisease(String disease);
}
