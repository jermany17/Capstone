package com.example.capstone.aidisease.service;

import com.example.capstone.aidisease.domain.Hospital;
import com.example.capstone.aidisease.dto.HospitalResponse;
import com.example.capstone.aidisease.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public Optional<HospitalResponse> getHospitalByLocation(String location) {
        return hospitalRepository.findByLocation(location)
                .map(h -> HospitalResponse.builder()
                        .location(h.getLocation())
                        .hospital1(h.getHospital1())
                        .hospital1Link(h.getHospital1Link())
                        .hospital2(h.getHospital2())
                        .hospital2Link(h.getHospital2Link())
                        .hospital3(h.getHospital3())
                        .hospital3Link(h.getHospital3Link())
                        .build());
    }
}
