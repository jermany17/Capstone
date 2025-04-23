package com.example.capstone.community.repository;

import com.example.capstone.community.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByOrderByCreateAtDesc(Pageable pageable);

    // 좋아요 수 내림차순 정렬
    @Query("SELECT p FROM Post p LEFT JOIN p.likes l GROUP BY p.id ORDER BY COUNT(l) DESC, p.createAt DESC")
    Page<Post> findAllOrderByLikesCountDesc(Pageable pageable);

    // 댓글 수 내림차순 정렬
    @Query("SELECT p FROM Post p LEFT JOIN p.comments c GROUP BY p.id ORDER BY COUNT(c) DESC, p.createAt DESC")
    Page<Post> findAllOrderByCommentsCountDesc(Pageable pageable);
}
