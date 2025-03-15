package com.example.capstone.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserPassword {
    private String currentPassword;
    private String newPassword;
}
