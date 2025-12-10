package com.mycom.myapp.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycom.myapp.domain.User;


public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
}
