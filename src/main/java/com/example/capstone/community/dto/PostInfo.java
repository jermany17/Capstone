package com.example.capstone.community.dto;

import com.example.capstone.community.domain.Comment;
import com.example.capstone.community.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostInfo {
    private final Long id;
    private final String title;
    private final String userId;
    private final String userName;
    private final List<String> images;
    private final String content;
    private final LocalDateTime createAt;
    private final LocalDateTime updateAt;
    private final List<String> likes;
    private final List<CommentDto> comments;

    public PostInfo(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.userId = post.getUserId();
        this.userName = post.getUserName();
        this.images = post.getImages();
        this.content = post.getContent();
        this.createAt = post.getCreateAt();
        this.updateAt = post.getUpdateAt();
        this.likes = post.getLikes();
        this.comments = post.getComments().stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class CommentDto {
        private final Long id;
        private final String userName;
        private final String content;
        private final LocalDateTime createAt;

        public CommentDto(Comment comment) {
            this.id = comment.getId();
            this.userName = comment.getUserName();
            this.content = comment.getContent();
            this.createAt = comment.getCreateAt();
        }
    }
}
