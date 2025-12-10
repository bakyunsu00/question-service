package com.mycom.myapp.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycom.myapp.auth.dto.LoginRequestDto;
import com.mycom.myapp.auth.dto.RegisterDto;
import com.mycom.myapp.auth.repository.UserRepository;
import com.mycom.myapp.domain.User;
import com.mycom.myapp.domain.enums.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterDto dto) {

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .role(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);
    }
    
    public User login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("아이디 없음"));

        // ✅ 여기서 네가 겪은 오류 발생함
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호 틀림");
        }

        return user;
    }
}