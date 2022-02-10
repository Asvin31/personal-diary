package com.diary.mapper;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.diary.data.RegisterRequest;
import com.diary.model.UserDao;

@Component
public class UserMapper {

	static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

	@Autowired
	PasswordEncoder passwordEncoder;

	public UserDao convertRequestToUser(RegisterRequest registerRequest) {
		return new UserDao(registerRequest.getUserName(), passwordEncoder.encode(registerRequest.getPassword()),
				registerRequest.getRoles());
	}

}
