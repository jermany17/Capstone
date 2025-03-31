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

    // 게시물 생성
    @PostMapping("/create")
    public ResponseEntity<Map<String, ?>> createPost(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            Authentication authentication
    ) {
        if (title == null || title.trim().isEmpty() || title.length() > 20) {
            return ResponseEntity.badRequest().body(Map.of("message", "제목은 1자 이상 20자 이하여야 합니다."));
        }

        if (files != null) {
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("message", "비어 있는 이미지 파일이 포함되어 있습니다."));
                }

                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.badRequest().body(Map.of("message", "이미지는 10MB를 초과할 수 없습니다."));
                }
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

    // 게시물 조회(size, page)
    @GetMapping("/read-latest")
    public ResponseEntity<List<PostInfo>> getAllPostsPaged(
            @RequestParam("size") int size,
            @RequestParam("page") int page
    ) {
        return ResponseEntity.ok(postService.getLatestPostsPaged(page, size));
    }

    // 게시물 단일 조회
    @GetMapping("/read-one/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            PostInfo postInfo = postService.getPostById(id);
            return ResponseEntity.ok(postInfo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        }
    }

    // 게시물 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {
        try {
            postService.deletePost(id, authentication);
            return ResponseEntity.ok(Map.of("message", "게시물이 성공적으로 삭제되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
        }
    }
}
