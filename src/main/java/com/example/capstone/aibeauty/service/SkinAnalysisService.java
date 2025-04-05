package com.example.capstone.aibeauty.service;

import com.example.capstone.aibeauty.dto.SkinAnalysisRequest;
import com.example.capstone.aibeauty.repository.SkinAnalysisResultRepository;
import com.example.capstone.aibeauty.domain.SkinAnalysisResult;
import com.example.capstone.auth.domain.User;
import com.example.capstone.awss3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;
import java.awt.image.BufferedImage;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SkinAnalysisService {

    private final SkinAnalysisResultRepository repository;
    private final S3Service s3Service;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String aiServerUrl = "https://59ef-223-195-38-166.ngrok-free.app/beauty"; // AI 서버 주소

    public String analyze(SkinAnalysisRequest request, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        String userId = user.getUserId(); // 현재 로그인 되어 있는 사용자
        String analysisId = randomUUID().toString(); // 분석 결과 고유 id 생성

        // 이미지 요청 정보를 Map으로 구성 (정면, 위, 아래 등)
        Map<String, MultipartFile> imageMap = new LinkedHashMap<>();
        imageMap.put("top", request.getTop());
        imageMap.put("front", request.getFront());
        imageMap.put("bottom", request.getBottom());
        imageMap.put("left15", request.getLeft15());
        imageMap.put("left30", request.getLeft30());
        imageMap.put("right15", request.getRight15());
        imageMap.put("right30", request.getRight30());

        // 이미지 없는 경우 검사 = null 체크 (getKey()로 어디 각도 이미지 안 들어왔는지 에러 메시지에 표시)
        for (Map.Entry<String, MultipartFile> entry : imageMap.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException(entry.getKey() + " 이미지가 null입니다.");
            }
        }

        // 업로드된 이미지의 S3 URL을 담을 Map = (정면, 위, 아래 등)이랑 이미지 URL 을 키-값 쌍으로 저장
        Map<String, String> imageUrlMap = new LinkedHashMap<>();

        // 모든 이미지 유효성 검사 먼저 수행
        for (MultipartFile file : imageMap.values()) {
            validateImage(file);
        }

        // 같은 날 기존 분석 결과가 있으면 삭제 (최신 분석 결과만 유지하기 위함)
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
        repository.deleteByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay);

        // 유효성 검사 통과 후 s3 업로드 수행
        for (Map.Entry<String, MultipartFile> entry : imageMap.entrySet()) {
            String key = entry.getKey();
            MultipartFile file = entry.getValue();
            String customPath = String.format("skin-analysis/%s/%s/%s", userId, analysisId, key); // 경로 직접 지정
            String s3Url = s3Service.uploadImageWithCustomPath(file, customPath);
            imageUrlMap.put(key, s3Url);
        }

        // AI 서버에 이미지 분석 요청
        Map<String, Integer> aiResult = callAiServer(imageMap);

        // 분석 결과를 DB에 저장
        SkinAnalysisResult result = SkinAnalysisResult.builder()
                .analysisId(analysisId)
                .userId(userId)
                .imageUrls(new ArrayList<>(imageUrlMap.values()))
                .totalWrinkle(aiResult.get("totalWrinkle"))
                .totalPigmentation(aiResult.get("totalPigmentation"))
                .totalPore(aiResult.get("totalPore"))
                .foreheadWrinkle(aiResult.get("foreheadWrinkle"))
                .foreheadPigmentation(aiResult.get("foreheadPigmentation"))
                .foreheadMoisture(aiResult.get("foreheadMoisture"))
                .foreheadElasticity(aiResult.get("foreheadElasticity"))
                .glabellaWrinkle(aiResult.get("glabellaWrinkle"))
                .lefteyeWrinkle(aiResult.get("lefteyeWrinkle"))
                .righteyeWrinkle(aiResult.get("righteyeWrinkle"))
                .leftcheekPigmentation(aiResult.get("leftcheekPigmentation"))
                .leftcheekPore(aiResult.get("leftcheekPore"))
                .leftcheekMoisture(aiResult.get("leftcheekMoisture"))
                .leftcheekElasticity(aiResult.get("leftcheekElasticity"))
                .rightcheekPigmentation(aiResult.get("rightcheekPigmentation"))
                .rightcheekPore(aiResult.get("rightcheekPore"))
                .rightcheekMoisture(aiResult.get("rightcheekMoisture"))
                .rightcheekElasticity(aiResult.get("rightcheekElasticity"))
                .lipDryness(aiResult.get("lipDryness"))
                .jawlineSagging(aiResult.get("jawlineSagging"))
                .chinMoisture(aiResult.get("chinMoisture"))
                .chinElasticity(aiResult.get("chinElasticity"))
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(result);
        //분석 결과 고유 id 반환
        return analysisId;
    }

    // 이미지 유효성 검사 메서드
    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("비어 있는 이미지 파일이 포함되어 있습니다.");
        }

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("이미지는 10MB를 초과할 수 없습니다.");
        }

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("올바르지 않은 이미지 형식입니다.");
            }
            if (image.getWidth() < 100 || image.getHeight() < 100) { // 일단 테스트를 위해서 100x100으로 설정 해놓음
                throw new IllegalArgumentException("이미지 해상도는 최소 1000x1000 이상이어야 합니다.");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 해상도 검사 중 오류가 발생했습니다.");
        }
    }

    // AI 서버 호출 메서드
    private Map<String, Integer> callAiServer(Map<String, MultipartFile> imageFileMap) {
        HttpHeaders headers = new HttpHeaders(); // 헤더 정보 구성
        headers.setContentType(MediaType.MULTIPART_FORM_DATA); // 보내는 데이터 타입 명시

        // 요청 본문에 들어갈 키-값 쌍을 저장 (정면, 위, 아래 등 key로 지정하고 값은 이미지 파일)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (Map.Entry<String, MultipartFile> entry : imageFileMap.entrySet()) {
            body.add(entry.getKey(), entry.getValue().getResource());
        } // .getResource() = 이미지 파일을 Spring이 전송 가능한 리소스 객체로 변환

        // 본문 + 헤더 합치기
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Integer>> response = restTemplate.exchange(
                aiServerUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }
}
