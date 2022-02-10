package com.diary.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diary.data.RegisterRequest;
import com.diary.data.SaveContent;
import com.diary.mapper.UserMapper;
import com.diary.model.Content;
import com.diary.model.UserDao;
import com.diary.repo.ContentRepo;
import com.diary.repo.UserRepo;
import com.diary.service.UserService;
import com.diary.utils.UtilsService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	UtilsService utilsService;

	@Autowired
	UserRepo userRepo;

	@Autowired
	ContentRepo contentRepo;

	static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

	@Override
	public Boolean registerUser(RegisterRequest registerRequest) {
		UserDao userDao = utilsService.checkUserExist(registerRequest.getUserName());
		if (userDao == null) {
			userRepo.save(userMapper.convertRequestToUser(registerRequest));
			return true;
		}
		return false;
	}

	@Override
	public Content saveContentForDate(SaveContent saveContent, String userName) {
		Content content = new Content();
		Optional<Content> existingContent = contentRepo.findContentForUser(userName, saveContent.getContentDate());
		if(existingContent.isEmpty()) {
			int month = LocalDate.parse(saveContent.getContentDate(), dateTimeFormatter).getMonthValue();
			int year = LocalDate.parse(saveContent.getContentDate(), dateTimeFormatter).getYear();
			List<String> newContent = new ArrayList<>();
			content.setUserName(userName);
			content.setDate(saveContent.getContentDate());
			content.setMonth(String.valueOf(month));
			content.setYear(String.valueOf(year));
			content.setLastUpdated(LocalDateTime.now());
			newContent.add(saveContent.getContent());
			content.setContent(newContent);
		}
		else {
			content = existingContent.get();
			List<String> existingContentData = content.getContent();
			existingContentData.add(saveContent.getContent());
			content.setContent(existingContentData);
			content.setLastUpdated(LocalDateTime.now());
		}
		contentRepo.save(content);
		return content;
	}

}
