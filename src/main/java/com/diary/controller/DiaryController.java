package com.diary.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.diary.data.AuthRequest;
import com.diary.data.ContentResponse;
import com.diary.data.EntryResponse;
import com.diary.data.ErrorData;
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
			Cookie cookie = new Cookie("jwt", jwt);
			response.addCookie(cookie);
			return ResponseEntity.status(200).body("Login Sucess");
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
			Content content = userService.saveContentForDate(saveContent, userDetails.getUsername(),
					saveContent.getContentId());
			if (content != null) {
				return ResponseEntity.status(200).body(content);
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
		}
		return ResponseEntity.status(500).body(null);
	}

	@GetMapping("/getDetailsForMonth/{month}/{year}")
	public ResponseEntity<List<String>> getDetailsForMonth(@PathVariable @NotEmpty String month,
			@PathVariable @NotEmpty String year) {
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			List<String> dateEntries = userService.getDatesForMonth(userDetails.getUsername(), month, year);
			if (dateEntries != null) {
				return ResponseEntity.status(200).body(dateEntries);
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
		}
		return ResponseEntity.status(500).body(null);
	}

	@GetMapping("/getDetailsForDate/{date}")
	public ResponseEntity<ContentResponse> getDetailsForDate(@PathVariable @NotEmpty String date) {
		ContentResponse contentResponse = new ContentResponse(null, null);
		int statusCode = 200;
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, EntryResponse> content = userService.getDateContent(userDetails.getUsername(), date);
			if (isNullOrEmptyMap(content)) {
				statusCode = 204;
			}
			contentResponse.setContent(content);
		} catch (Exception e) {
			statusCode = 500;
			contentResponse.setErrorData(new ErrorData(500, e.getMessage()));
		}
		return ResponseEntity.status(statusCode).body(contentResponse);
	}

	@DeleteMapping("/deleteEntry/{date}/{contentId}")
	public ResponseEntity<ContentResponse> deleteEntry(@PathVariable @NotEmpty String date,
			@PathVariable @NotEmpty String contentId) {
		ContentResponse contentResponse = new ContentResponse(null, null);
		int statusCode = 200;
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			contentResponse.setContent(userService.removeEntry(userDetails.getUsername(), date, contentId));
		} catch (Exception e) {
			e.printStackTrace();
			contentResponse.setErrorData(new ErrorData(500, e.getMessage()));
		}
		return ResponseEntity.status(statusCode).body(contentResponse);
	}

	@DeleteMapping("/deleteAllEntry/{date}")
	public ResponseEntity<String> deleteAllEntry(@PathVariable @NotEmpty String date) {
		int statusCode = 200;
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			return ResponseEntity.status(statusCode)
					.body(userService.removeAllEntry(userDetails.getUsername(), date).toString());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}
	
	@GetMapping("/removeAuth")
	public ResponseEntity<String> logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("jwt", null);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return ResponseEntity.status(200).body("Logout sucess");
	}

	public static boolean isNullOrEmptyMap(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}
}
