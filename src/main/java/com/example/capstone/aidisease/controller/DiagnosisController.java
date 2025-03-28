package com.example.capstone.aidisease.controller;

import com.example.capstone.aidisease.dto.DiagnosisResultResponse;
import com.example.capstone.aidisease.service.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skin-diagnosis")
public class DiagnosisController {
    private final DiagnosisService diagnosisService;

    @PostMapping
    public ResponseEntity<?> diagnose(@RequestParam("image") MultipartFile file) { //"image" 키에 해당하는 값

        //비어 있는 파일 검사
        if (file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "비어 있는 이미지 파일이 포함되어 있습니다."));
        }

        //파일 크기 검사 (10MB 초과 금지)
        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "이미지는 10MB를 초과할 수 없습니다."));
        }

        //피부질환진단
        DiagnosisResultResponse result = diagnosisService.diagnose(file);
        return ResponseEntity.ok(result);
    }
}
