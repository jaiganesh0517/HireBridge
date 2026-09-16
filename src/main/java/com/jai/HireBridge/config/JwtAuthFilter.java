package com.jai.HireBridge.config;

import java.io.IOException;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter
{
 
	private final JwtUtil jwtUtil;
	
	public JwtAuthFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
			                        @NonNull HttpServletResponse response,
			                         @NonNull FilterChain filterChain) throws IOException, ServletException {
		String authHeader = request.getHeader("Authorization");
		if(authHeader != null && authHeader.startsWith("Bearer")) {
		  String token = authHeader.substring(7);
			if(jwtUtil.isTokenValid(token)) {
			   Long userId = jwtUtil.extractUserId(token);
			   String role = jwtUtil.extractRole(token);
			   
			   UsernamePasswordAuthenticationToken authToken= 
					   new UsernamePasswordAuthenticationToken(userId,null,List.of(new SimpleGrantedAuthority("ROLE_"+ role)));
			   
			   SecurityContextHolder.getContext().setAuthentication(authToken);
			   
		    }
		}
		filterChain.doFilter(request, response);
	}
	
}
