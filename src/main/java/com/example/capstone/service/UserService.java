package com.example.capstone.service;

import com.example.capstone.domain.User;
import com.example.capstone.dto.AddUser;
import com.example.capstone.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원 정보 저장
    public Long save(AddUser dto) {
        User user = User.builder()
                .nickName(dto.getNickName())
                .userId(dto.getUserId())
                .userPassword(bCryptPasswordEncoder.encode(dto.getUserPassword())) // 비밀번호 암호화
                .build();
        return userRepository.save(user).getId();
    }

    // userId 중복 확인
    public boolean isUserIdExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(User user, String encodedNewPassword) {
        user.changePassword(encodedNewPassword);
        userRepository.save(user);
    }

    // 회원 삭제
    @Transactional
    public void deleteUser(String userId) {
        userRepository.deleteByUserId(userId);
    }
}
