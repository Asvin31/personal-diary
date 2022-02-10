package com.diary.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import com.diary.utils.JwtUserDetails;
import com.diary.utils.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	JwtUserDetails jwtUserDetails;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		String userName = null;
		String jwt = null;
		Cookie tokenCookie = WebUtils.getCookie(request, "jwt");
		if (tokenCookie != null) {
			jwt = tokenCookie.getValue();
			userName = jwtUtil.getUsernameFromToken(jwt);
		} else {
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
				jwt = authorizationHeader.substring(7);
				userName = jwtUtil.getUsernameFromToken(jwt);
			}
		}
		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = jwtUserDetails.loadUserByUsername(userName);
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
		filterChain.doFilter(request, response);
	}

}
