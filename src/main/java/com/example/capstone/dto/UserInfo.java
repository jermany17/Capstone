package com.example.capstone.dto;

import com.example.capstone.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private String nickName;
    private String userId;
    private String createAt;
    private String updateAt;

    public UserInfo(User user) {
        this.nickName = user.getNickName();
        this.userId = user.getUserId();
        this.createAt = user.getCreateAt().toString();
        this.updateAt = user.getUpdateAt().toString();
    }
}
