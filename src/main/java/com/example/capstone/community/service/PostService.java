package com.example.capstone.community.service;

import com.example.capstone.community.domain.Post;
import com.example.capstone.auth.domain.User;
import com.example.capstone.community.dto.PostInfo;
import com.example.capstone.community.repository.PostRepository;
import com.example.capstone.awss3.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public List<PostInfo> getLatestPostsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Post> postPage = postRepository.findAllByOrderByCreateAtDesc(pageable);

        return postPage.getContent().stream()
                .map(PostInfo::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostInfo getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));
        return new PostInfo(post);
    }

    @Transactional
    public void deletePost(Long id, Authentication authentication) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        User user = (User) authentication.getPrincipal();
        if (!post.getUserId().equals(user.getUserId())) {
            throw new SecurityException("게시물 삭제 권한이 없습니다.");
        }

        // S3에 저장된 이미지가 있다면 삭제
        if (post.getImages() != null) {
            for (String imageUrl : post.getImages()) {
                s3Service.deleteImage(imageUrl);
            }
        }

        postRepository.delete(post);
    }
}
