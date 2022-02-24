package com.diary.service;

import java.util.List;
import java.util.Map;

import com.diary.data.EntryResponse;
import com.diary.data.RegisterRequest;
import com.diary.data.SaveContent;
import com.diary.exception.ContentNotFoundException;
import com.diary.model.Content;

public interface UserService {
	Boolean registerUser(RegisterRequest registerRequest);

	Content saveContentForDate(SaveContent saveContent, String userName, String contentId);

	List<String> getDatesForMonth(String userName, String month, String year);

	Map<String, EntryResponse> getDateContent(String userName, String date);

	Map<String, EntryResponse> removeEntry(String userName, String date, String contentId)
			throws ContentNotFoundException;

	Boolean removeAllEntry(String userName, String date) throws ContentNotFoundException;
}
