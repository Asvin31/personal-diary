package com.diary.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diary.model.UserDao;
import com.diary.repo.UserRepo;

@Service
public class UtilsService {

	@Autowired
	UserRepo userRepo;

	public UserDao checkUserExist(String userName) {
		return userRepo.findByUserName(userName);
	}

}
