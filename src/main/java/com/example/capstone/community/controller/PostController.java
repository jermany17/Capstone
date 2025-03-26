package com.example.capstone.community.controller;

import com.example.capstone.community.dto.PostInfo;
import com.example.capstone.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Map<String, ?>> createPost(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            Authentication authentication
    ) {
        if (title == null || title.trim().isEmpty() || title.length() > 20) {
            return ResponseEntity.badRequest().body(Map.of("message", "제목은 1자 이상 20자 이하여야 합니다."));
        }

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미지를 최소 1개 이상 업로드해야 합니다."));
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "비어 있는 이미지 파일이 포함되어 있습니다."));
            }

            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.badRequest().body(Map.of("message", "이미지는 10MB를 초과할 수 없습니다."));
            }
        }

        try {
            Long postId = postService.createPost(title, content, files, authentication);
            return ResponseEntity.ok(Map.of("id", postId));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("message", "파일 처리 중 오류가 발생했습니다: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "게시물 등록 중 예상치 못한 오류가 발생했습니다."));
        }
    }

    @GetMapping
    public ResponseEntity<List<PostInfo>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }
}
