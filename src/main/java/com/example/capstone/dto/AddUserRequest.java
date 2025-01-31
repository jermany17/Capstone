package com.example.capstone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String nickName; // 사용자 이름
    private String userId; // 로그인 ID
    private String userPassword; // 비밀번호
}
