package com.mycom.myapp.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mycom.myapp.auth.config.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {
	
	private final CustomUserDetailsService customUserDetailsService;
	
	@Value("${myapp.jwt.secret}")
	private String secretKeyStr;
	private SecretKey secretKey;
	private final long tokenValidDuration = 1000L* 60 * 60;
	
	@PostConstruct
	protected void init() {
		secretKey = new SecretKeySpec(
				secretKeyStr.getBytes(StandardCharsets.UTF_8), 
				Jwts.SIG.HS256.key().build().getAlgorithm()
		);
		log.info("[JwtUtil] SecretKey 생성");
	}
	
	public String createToken(String username, String role) {
		
		Date now = new Date();
		
		String token = Jwts.builder()
				.subject(username)
				.claim("role", role)
				.issuedAt(now)
				.expiration(new Date(now.getTime()+tokenValidDuration))
				.signWith(secretKey, Jwts.SIG.HS256)
				.compact();
		
		log.debug("[JwtUtil] token 생성 username = {}, role = {}", username,role);
		
		return token;
	}
	
	public UsernamePasswordAuthenticationToken getAuthentication(String token) {
		String username = this.getUsernameFromToken(token);
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		log.debug("[JwtUtil] 인증 username = {}", username);
		return new UsernamePasswordAuthenticationToken(
				userDetails.getUsername(), "",userDetails.getAuthorities());
	}
	
	public String getUsernameFromToken(String token) {
		String subject = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token).getPayload()
				.getSubject();
		
		return subject;
	}
	
	public String getTokenFromHeader(HttpServletRequest request) {
		return request.getHeader("X-AUTH-TOKEN");
	}
	
	public Claims validateToken(String token) {
		try {
			Claims claims = Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token)
					.getPayload();
			
			if(claims.getExpiration() != null && claims.getExpiration().before(new Date())) {
				log.warn("[JwtUtil] 토큰 만료");
				return null;
			}
			
			return claims;
			
		} catch (Exception e) {
			log.warn("[JwtUtil] 토큰 유효x");
			return null;
		}
	}
}
