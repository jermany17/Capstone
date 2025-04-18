package com.example.capstone.aidisease.controller;

import com.example.capstone.aidisease.dto.DiagnosisResultResponse;
import com.example.capstone.aidisease.dto.DiseaseDetailResponse;
import com.example.capstone.aidisease.service.DiseaseDetailService;
import com.example.capstone.aidisease.service.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skin-diagnosis")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;
    private final DiseaseDetailService diseaseDetailService;

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

        //이미지 해상도 검사
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "올바르지 않은 이미지 형식입니다."));
            }
            if (image.getWidth() < 200 || image.getHeight() < 200) {
                return ResponseEntity.badRequest().body(Map.of("message", "이미지 해상도는 최소 300x300 이상이어야 합니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미지 해상도 검사 중 오류가 발생했습니다."));
        }

        //피부질환진단
        DiagnosisResultResponse result = diagnosisService.diagnose(file);
        return ResponseEntity.ok(result);
    }

    // 피부 질환 추가 정보
    @GetMapping("/detail")
    public ResponseEntity<?> getDiseaseDetail(@RequestParam String disease) {
        try {
            DiseaseDetailResponse response = diseaseDetailService.getDiseaseDetailByName(disease);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("질환 정보를 불러오는 과정에서 오류가 발생했습니다.");
        }
    }
}
