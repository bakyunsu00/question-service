package com.mycom.myapp.auth.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = jwtUtil.getTokenFromHeader(request);
		
		Claims claims = null;
		if(token != null) {
			claims = jwtUtil.validateToken(token);
			
			if(claims != null) {
				String username = claims.getSubject();
				
				UsernamePasswordAuthenticationToken authentication =
						jwtUtil.getAuthentication(token);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.debug("[JwtFilter] 인증 완료 url = {}",request.getRequestURI());
			}
			else {
				log.debug("[JwtFilter] 토큰 유효x url = {}",request.getRequestURI());
			}
		}		
		
		filterChain.doFilter(request, response);
	}
	
}
