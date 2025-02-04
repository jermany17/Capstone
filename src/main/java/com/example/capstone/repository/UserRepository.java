package com.example.capstone.repository;

import com.example.capstone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId); // user_id로 조회

    boolean existsByNickName(String nickName); // nickName 중복 여부 확인
    boolean existsByUserId(String userId); // userId 중복 여부 확인
}
