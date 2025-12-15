package com.mycom.myapp.auth.config;

import com.mycom.myapp.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mycom.myapp.auth.repository.UserRepository;

import com.mycom.myapp.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("사용자 없음"));
		
		return new CustomUserDetails(user);
	}
}
