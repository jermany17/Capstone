package com.example.capstone.aidisease.service;

import com.example.capstone.aidisease.domain.Hospital;
import com.example.capstone.aidisease.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public Optional<Hospital> getHospitalByLocation(String location) {
        return hospitalRepository.findByLocation(location);
    }
}