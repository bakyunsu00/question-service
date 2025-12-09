package com.mycom.myapp.domain;

import com.mycom.myapp.domain.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users") // DB 예약어 피하기 위해 s 붙임
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // 로그인 ID

    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole role; // ROLE_USER, ROLE_ADMIN
}