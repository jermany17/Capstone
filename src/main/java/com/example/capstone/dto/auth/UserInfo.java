package com.example.capstone.dto.auth;

import com.example.capstone.domain.auth.User;
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
