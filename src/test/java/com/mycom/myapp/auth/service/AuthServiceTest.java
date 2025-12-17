package com.mycom.myapp.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mycom.myapp.auth.dto.LoginRequestDto;
import com.mycom.myapp.auth.dto.RegisterDto;
import com.mycom.myapp.auth.jwt.JwtUtil;
import com.mycom.myapp.auth.repository.UserRepository;
import com.mycom.myapp.domain.admin.User;
import com.mycom.myapp.domain.enums.UserRole;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    /* =========================
       login 테스트
     ========================= */

    @Test
    @DisplayName("로그인 성공 시 JWT 토큰 반환")
    void login_success() {
        // given
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("user1");
        dto.setPassword("1234");

        User user = User.builder()
                .username("user1")
                .password("encodedPw")
                .role(UserRole.ROLE_USER)
                .build();

        when(userRepository.findByUsername("user1"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("1234", "encodedPw"))
                .thenReturn(true);

        when(jwtUtil.createToken("user1", "ROLE_USER"))
                .thenReturn("jwt-token");

        // when
        String token = authService.login(dto);

        // then
        assertEquals("jwt-token", token);
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 아이디")
    void login_fail_user_not_found() {
        // given
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("noUser");
        dto.setPassword("1234");

        when(userRepository.findByUsername("noUser"))
                .thenReturn(Optional.empty());

        // when & then
        RuntimeException e = assertThrows(RuntimeException.class,
                () -> authService.login(dto));

        assertEquals("아이디 없음", e.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_fail_password_mismatch() {
        // given
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUsername("user1");
        dto.setPassword("wrongPw");

        User user = User.builder()
                .username("user1")
                .password("encodedPw")
                .role(UserRole.ROLE_USER)
                .build();

        when(userRepository.findByUsername("user1"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongPw", "encodedPw"))
                .thenReturn(false);

        // when & then
        RuntimeException e = assertThrows(RuntimeException.class,
                () -> authService.login(dto));

        assertEquals("비밀번호 틀림", e.getMessage());
    }

    /* =========================
       getUserRole 테스트
     ========================= */

    @Test
    @DisplayName("유저 권한 조회 성공")
    void getUserRole_success() {
        // given
        User user = User.builder()
                .username("admin")
                .role(UserRole.ROLE_ADMIN)
                .build();

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        // when
        String role = authService.getUserRole("admin");

        // then
        assertEquals("ROLE_ADMIN", role);
    }

    /* =========================
       register 테스트
     ========================= */

    @Test
    @DisplayName("회원가입 성공")
    void register_success() {
        // given
        RegisterDto dto = new RegisterDto();
        dto.setUsername("user1");
        dto.setPassword("password123");
        dto.setNickname("닉네임");

        when(userRepository.existsByUsername("user1"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPw");

        // when
        authService.register(dto);

        // then
        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void register_fail_duplicate_username() {
        // given
        RegisterDto dto = new RegisterDto();
        dto.setUsername("user1");
        dto.setPassword("password123");
        dto.setNickname("닉네임");

        when(userRepository.existsByUsername("user1"))
                .thenReturn(true);

        // when & then
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(dto)
        );

        assertEquals("이미 존재하는 아이디", e.getMessage());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 길이 부족")
    void register_fail_short_password() {
        // given
        RegisterDto dto = new RegisterDto();
        dto.setUsername("user1");
        dto.setPassword("1234");
        dto.setNickname("닉네임");

        when(userRepository.existsByUsername("user1"))
                .thenReturn(false);

        // when & then
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(dto)
        );

        assertEquals("비밀번호는 8자 이상", e.getMessage());
    }

    /* =========================
       아이디 중복 체크
     ========================= */

    @Test
    @DisplayName("아이디 사용 가능 여부 확인")
    void isUsernameAvailable_test() {
        // given
        when(userRepository.existsByUsername("user1"))
                .thenReturn(false);

        // when
        boolean result = authService.isUsernameAvailable("user1");

        // then
        assertTrue(result);
    }
}