package com.example.capstone.auth.dto;

import com.example.capstone.auth.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private String userName;
    private String userId;
    private String createAt;
    private String updateAt;

    public UserInfo(User user) {
        this.userName = user.getUserName();
        this.userId = user.getUserId();
        this.createAt = user.getCreateAt().toString();
        this.updateAt = user.getUpdateAt().toString();
    }
}
