package com.example.capstone.skincaremission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyMissionResponse {
    private String mission;
    private boolean checked;
}
