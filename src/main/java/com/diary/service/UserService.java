package com.diary.service;

import com.diary.data.RegisterRequest;
import com.diary.data.SaveContent;
import com.diary.model.Content;

public interface UserService {
	Boolean registerUser(RegisterRequest registerRequest);
	Content saveContentForDate(SaveContent saveContent, String userName);
}
