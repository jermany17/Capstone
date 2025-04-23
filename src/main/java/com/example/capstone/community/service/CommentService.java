package com.example.capstone.community.service;

import com.example.capstone.auth.domain.User;
import com.example.capstone.community.domain.Comment;
import com.example.capstone.community.domain.Post;
import com.example.capstone.community.repository.CommentRepository;
import com.example.capstone.community.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createComment(Long postId, String content, Authentication authentication) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        User user = (User) authentication.getPrincipal();

        Comment comment = Comment.builder()
                .content(content)
                .userId(user.getUserId())
                .userName(user.getUserName())
                .post(post)
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, String content, Authentication authentication) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        User user = (User) authentication.getPrincipal();
        if (!comment.getUserId().equals(user.getUserId())) {
            throw new SecurityException("댓글 수정 권한이 없습니다.");
        }

        comment.updateContent(content);
    }

    @Transactional
    public void deleteComment(Long commentId, Authentication authentication) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        User user = (User) authentication.getPrincipal();
        if (!comment.getUserId().equals(user.getUserId())) {
            throw new SecurityException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
