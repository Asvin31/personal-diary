package com.diary.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.diary.data.AuthRequest;
import com.diary.data.RegisterRequest;
import com.diary.data.SaveContent;
import com.diary.model.Content;
import com.diary.service.UserService;
import com.diary.utils.JwtUserDetails;
import com.diary.utils.JwtUtil;

@RestController
public class DiaryController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUserDetails jwtUserDetails;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UserService userService;

	@PostMapping("/authenticate")
	public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
			final UserDetails userDetails = jwtUserDetails.loadUserByUsername(authRequest.getUserName());
			final String jwt = jwtUtil.generateToken(userDetails);
			response.addCookie(new Cookie("jwt", jwt));
			return ResponseEntity.status(200).body("Login Sucess");
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
		return Boolean.TRUE.equals(userService.registerUser(registerRequest)) ? ResponseEntity.status(200).body("ok")
				: ResponseEntity.status(500).body("Registration Failed");
	}

	@PostMapping("/save")
	public ResponseEntity<Content> getInfo(@RequestBody SaveContent saveContent) {
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Content content = userService.saveContentForDate(saveContent, userDetails.getUsername());
			if (content != null) {
				return ResponseEntity.status(200).body(content);
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
		}
		return ResponseEntity.status(500).body(null);
	}

}
