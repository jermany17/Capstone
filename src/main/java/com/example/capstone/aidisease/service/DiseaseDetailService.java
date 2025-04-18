package com.example.capstone.aidisease.service;

import com.example.capstone.aidisease.domain.DiseaseDetail;
import com.example.capstone.aidisease.dto.DiseaseDetailResponse;
import com.example.capstone.aidisease.repository.DiseaseDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiseaseDetailService {
    private final DiseaseDetailRepository diseaseDetailRepository;

    // 질환 이름으로 피부 질환 상세 정보 찾기
    public DiseaseDetailResponse getDiseaseDetailByName(String disease) {
        DiseaseDetail detail = diseaseDetailRepository.findByDisease(disease)
                .orElseThrow(() -> new IllegalArgumentException("해당 질환 정보를 찾을 수 없습니다."));

        return DiseaseDetailResponse.builder()
                .disease(detail.getDisease())
                .imageUrls(detail.getImageUrls())
                .definition(detail.getDefinition())
                .cause(detail.getCause())
                .symptom(detail.getSymptom())
                .source(detail.getSource())
                .build();
    }
}
