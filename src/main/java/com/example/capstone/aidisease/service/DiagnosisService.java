package com.example.capstone.aidisease.service;

import com.example.capstone.aidisease.domain.DiseaseTreatment;
import com.example.capstone.aidisease.dto.DiagnosisResultResponse;
import com.example.capstone.aidisease.repository.DiseaseTreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiagnosisService {
    private final DiseaseTreatmentRepository treatmentRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public DiagnosisResultResponse diagnose(MultipartFile file) {

        //AI 서버로 이미지 전송 (AI 서버 지금 무료 이용 중이라 수시로 바뀜)
        String aiUrl = "https://59ef-223-195-38-166.ngrok-free.app/disease"; //AI 서버 진단 API 주소
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA); //전송할 데이터 multipart/form-data 명시

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(); //요청 본문에 이미지 파일 추가
        body.add("image", file.getResource()); //file.getResource() = file을 리소스로 변환

        //최종 요청 객체 구성 (본문 + 헤더 합치기)
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        //RestTemplate을 이용해 AI 서버에 POST 요청 전송
        //응답은 JSON 형태 Map으로 받아옴
        ResponseEntity<Map> aiResponse = restTemplate.postForEntity(aiUrl, requestEntity, Map.class);

        //AI 분석 결과 파싱
        String disease = (String) aiResponse.getBody().get("Disease");
        double probability = Double.parseDouble(aiResponse.getBody().get("Probability").toString());

        // 피부 질환이 아닌 경우 응답 빌드
        if ("피부질환없음".equals(disease)) {
            return DiagnosisResultResponse.builder()
                    .disease(disease)
                    .probability(probability)
                    .treatment("없음")
                    .source("없음")
                    .imageUrl("없음")
                    .build();
        }

        //DB에서 질환 정보 조회
        DiseaseTreatment treatmentInfo = treatmentRepository.findByDisease(disease)
                .orElseThrow(() -> new RuntimeException("등록된 질환 정보가 없습니다: " + disease));

        //응답 빌드
        return DiagnosisResultResponse.builder()
                .disease(disease)
                .probability(probability)
                .treatment(treatmentInfo.getTreatment())
                .source(treatmentInfo.getSource())
                .imageUrl(treatmentInfo.getImageUrl())
                .build();
    }
}
