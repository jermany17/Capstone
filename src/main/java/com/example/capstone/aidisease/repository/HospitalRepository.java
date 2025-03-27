package com.example.capstone.aidisease.repository;

import com.example.capstone.aidisease.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, String> {
    Optional<Hospital> findByLocation(String location);
}
