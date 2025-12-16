package com.mycom.myapp.auth.service;

import com.mycom.myapp.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycom.myapp.auth.dto.LoginRequestDto;
import com.mycom.myapp.auth.dto.RegisterDto;
import com.mycom.myapp.auth.jwt.JwtUtil;
import com.mycom.myapp.domain.admin.User;

import com.mycom.myapp.domain.enums.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public String login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("아이디 없음"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호 틀림");
        }
        
        String token = jwtUtil.createToken(user.getUsername(), user.getRole().name());

        return token;
    }

    public void register(RegisterDto dto) {
    	
    	if(userRepository.existsByUsername(dto.getUsername())) {
    		throw new IllegalArgumentException("이미 존재하는 아이디");
    	}
    	
    	if(dto.getPassword().length()<8) {
    		throw new IllegalArgumentException("비밀번호는 8자 이상");
    	}

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .role(UserRole.ROLE_USER)
                .build();

        userRepository.save(user);
    }
    
    public boolean isUsernameAvailable(String username) {		
		return !userRepository.existsByUsername(username);
	}
}