package com.example.capstone.aibeauty.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SkinAnalysisRequest {

    private MultipartFile top;       // 위
    private MultipartFile front;     // 정면
    private MultipartFile bottom;    // 아래
    private MultipartFile left15;    // 좌측 15도
    private MultipartFile left30;    // 좌측 30도
    private MultipartFile right15;   // 우측 15도
    private MultipartFile right30;   // 우측 30도
}
