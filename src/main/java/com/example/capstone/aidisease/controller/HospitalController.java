package com.example.capstone.aidisease.controller;

import com.example.capstone.aidisease.domain.Hospital;
import com.example.capstone.aidisease.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospital")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping("/{location}")
    public ResponseEntity<Hospital> getHospital(@PathVariable String location) {
        return hospitalService.getHospitalByLocation(location)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
