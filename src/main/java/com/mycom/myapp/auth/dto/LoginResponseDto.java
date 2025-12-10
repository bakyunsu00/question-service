package com.mycom.myapp.auth.dto;

import com.mycom.myapp.domain.enums.UserRole;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private UserRole role;
}