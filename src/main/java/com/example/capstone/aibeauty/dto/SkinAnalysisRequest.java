package com.example.capstone.aibeauty.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SkinAnalysisRequest {

    private MultipartFile front;   // 정면
    private MultipartFile left;    // 좌측
    private MultipartFile right;   // 우측
}
