package com.example.capstone.skincaremission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MissionScoreResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private int averageScore;
}
