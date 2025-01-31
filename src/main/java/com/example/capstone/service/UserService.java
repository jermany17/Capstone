package com.example.capstone.service;

import com.example.capstone.domain.User;
import com.example.capstone.dto.UserRequest;
import com.example.capstone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(UserRequest dto) {
        User user = User.builder()
                .nickName(dto.getNickName())
                .userId(dto.getUserId())
                .userPassword(bCryptPasswordEncoder.encode(dto.getUserPassword())) // 비밀번호 암호화
                .build();
        return userRepository.save(user).getId();
    }
}
