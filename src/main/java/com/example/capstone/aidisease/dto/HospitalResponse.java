package com.example.capstone.aidisease.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HospitalResponse {
    private final String location;
    private final String hospital1;
    private final String hospital1Link;
    private final String hospital2;
    private final String hospital2Link;
    private final String hospital3;
    private final String hospital3Link;
}
