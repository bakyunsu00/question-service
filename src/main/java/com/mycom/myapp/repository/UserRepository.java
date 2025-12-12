package com.mycom.myapp.repository;

import com.mycom.myapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 1. 로그인용: 아이디(username)로 회원 정보 찾기
    // 없을 수도 있으니까 Optional로 감싸서 반환하는 것이 국룰입니다.
    Optional<User> findByUsername(String username);

    // 2. 회원가입용: 이미 존재하는 아이디인지 검사 (중복 체크)
    boolean existsByUsername(String username);
}