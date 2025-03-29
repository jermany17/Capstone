package com.example.capstone.aidisease.controller;

import com.example.capstone.aidisease.dto.HospitalResponse;
import com.example.capstone.aidisease.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospital")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<HospitalResponse> getHospital(@RequestParam String location) {
        return hospitalService.getHospitalByLocation(location)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
