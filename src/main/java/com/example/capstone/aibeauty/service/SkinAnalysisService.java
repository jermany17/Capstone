package com.example.capstone.aibeauty.service;

import com.example.capstone.aibeauty.dto.SkinAnalysisRequest;
import com.example.capstone.aibeauty.repository.SkinAnalysisResultRepository;
import com.example.capstone.aibeauty.domain.SkinAnalysisResult;
import com.example.capstone.auth.domain.User;
import com.example.capstone.awss3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.*;
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

        // 이미지 요청 정보를 Map으로 구성 (정면, 좌측, 우측)
        Map<String, MultipartFile> imageMap = new LinkedHashMap<>();
        imageMap.put("front", request.getFront());
        imageMap.put("left", request.getLeft());
        imageMap.put("right", request.getRight());

        // 이미지 없는 경우 검사 = null 체크 (getKey()로 어디 각도 이미지 안 들어왔는지 에러 메시지에 표시)
        for (Map.Entry<String, MultipartFile> entry : imageMap.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("이미지 데이터 수신 과정에서 오류가 발생했습니다");
            }
        }

        // 업로드된 이미지의 S3 URL을 담을 Map = (정면, 좌측, 우측)이랑 이미지 URL 을 키-값 쌍으로 저장
        Map<String, String> imageUrlMap = new LinkedHashMap<>();

        // 모든 이미지 유효성 검사 먼저 수행
        for (MultipartFile file : imageMap.values()) {
            validateImage(file);
        }

        // 같은 날 기존 분석 결과가 있으면 삭제 (최신 분석 결과만 유지하기 위함)
        deleteExistingResultIfExists(userId, LocalDate.now());

        try {
            // 유효성 검사 통과 후 s3 업로드 수행
            for (Map.Entry<String, MultipartFile> entry : imageMap.entrySet()) {
                String key = entry.getKey();
                MultipartFile file = entry.getValue();
                String customPath = String.format("skin-analysis/%s/%s/%s", userId, analysisId, key); // 경로 직접 지정
                String s3Url = s3Service.uploadImageWithCustomPath(file, customPath);
                imageUrlMap.put(key, s3Url);
            }
        } catch (IOException | RuntimeException e) { // s3Service에서 발생할 수 있는 오류
            throw new RuntimeException("S3 이미지 업로드 중 오류가 발생했습니다.");
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
                .skinAge(aiResult.get("skinAge"))
                .foreheadWrinkle(aiResult.get("foreheadWrinkle"))
                .foreheadPigmentation(aiResult.get("foreheadPigmentation"))
                .glabellaWrinkle(aiResult.get("glabellaWrinkle"))
                .lefteyeWrinkle(aiResult.get("lefteyeWrinkle"))
                .righteyeWrinkle(aiResult.get("righteyeWrinkle"))
                .leftcheekPigmentation(aiResult.get("leftcheekPigmentation"))
                .leftcheekPore(aiResult.get("leftcheekPore"))
                .rightcheekPigmentation(aiResult.get("rightcheekPigmentation"))
                .rightcheekPore(aiResult.get("rightcheekPore"))
                .lipDryness(aiResult.get("lipDryness"))
                .jawlineSagging(aiResult.get("jawlineSagging"))
                .createdAt(LocalDateTime.now())
                .build();

        try {
            repository.save(result);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("분석 결과 저장 중 무결성 제약 조건 위반이 발생했습니다.", e);
        } catch (DataAccessException e) {
            throw new RuntimeException("분석 결과를 데이터베이스에 저장하는 중 오류가 발생했습니다.", e);
        }

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

    // 같은 날의 기존 분석 결과가 이미 존재하는 경우, 해당 결과의 이미지들을 S3에서 모두 삭제하고 DB에서도 해당 결과 삭제
    private void deleteExistingResultIfExists(String userId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        repository.findByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay)
                .ifPresent(existingResult -> {
                    try {
                        // 기존 분석 결과 있으면 이미지 URL들 순회하며 S3에서 삭제
                        for (String url : existingResult.getImageUrls()) {
                            s3Service.deleteImage(url);
                        }
                        repository.delete(existingResult); // DB 분석 결과 삭제
                    } catch (Exception e) {
                        // S3 또는 DB 삭제 중 오류 발생 시 전체 예외 던짐
                        throw new RuntimeException("기존 분석 결과 삭제 중 오류 발생가 발생했습니다.", e);
                    }
                });
    }

    // AI 서버 호출 메서드
    private Map<String, Integer> callAiServer(Map<String, MultipartFile> imageFileMap) {
        HttpHeaders headers = new HttpHeaders(); // 헤더 정보 구성
        headers.setContentType(MediaType.MULTIPART_FORM_DATA); // 보내는 데이터 타입 명시

        // 요청 본문에 들어갈 키-값 쌍을 저장 (정면, 좌측, 우측 key로 지정하고 값은 이미지 파일)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (Map.Entry<String, MultipartFile> entry : imageFileMap.entrySet()) {
            body.add(entry.getKey(), entry.getValue().getResource());
        } // .getResource() = 이미지 파일을 Spring이 전송 가능한 리소스 객체로 변환

        // 본문 + 헤더 합치기
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, Integer>> response = restTemplate.exchange(
                    aiServerUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("AI 서버에서 오류 응답이 발생했습니다. ", e);
        } catch (ResourceAccessException e) {
            throw new RuntimeException("AI 서버에 연결할 수 없습니다.", e);
        } catch (RestClientException e) {
            throw new RuntimeException("AI 서버 호출 중 오류가 발생했습니다.", e);
        }
    }
}
