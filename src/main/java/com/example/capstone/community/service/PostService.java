package com.example.capstone.community.service;

import com.example.capstone.community.domain.Post;
import com.example.capstone.auth.domain.User;
import com.example.capstone.community.dto.PostInfo;
import com.example.capstone.community.repository.PostRepository;
import com.example.capstone.awss3.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;

    @Transactional
    public Long createPost(String title, String content, List<MultipartFile> files, Authentication authentication) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        if (files != null) {
            for (MultipartFile file : files) {
                String url = s3Service.uploadImage(file);
                imageUrls.add(url);
            }
        }

        User user = (User) authentication.getPrincipal(); // 현재 로그인한 사용자 가져오기
        Post post = Post.builder()
                .title(title)
                .content(content)
                .images(imageUrls)
                .userId(user.getUserId())
                .userName(user.getUserName())
                .build();

        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Transactional
    public List<PostInfo> getAllPosts() {

        return postRepository.findAll().stream()
                .map(PostInfo::new)
                .collect(Collectors.toList());
    }
}
