package com.example.capstone.skincaremission.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TopMissionScoresResponse {
    private List<TopUserScoreDto> topUsers;
}
