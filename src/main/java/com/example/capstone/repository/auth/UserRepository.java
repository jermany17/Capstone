package com.example.capstone.repository.auth;

import com.example.capstone.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId); // user_id로 조회

    boolean existsByUserId(String userId); // userId 중복 여부 확인

    void deleteByUserId(String userId); // userId로 회원 삭제
}
