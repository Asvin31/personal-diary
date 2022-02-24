package com.diary.service.implementation;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.diary.data.EntryResponse;
import com.diary.data.RegisterRequest;
import com.diary.data.SaveContent;
import com.diary.exception.ContentNotFoundException;
import com.diary.mapper.UserMapper;
import com.diary.model.Content;
import com.diary.model.ContentSequence;
import com.diary.model.Entry;
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

	@Autowired
	MongoTemplate mongoTemplate;

	static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
	static final String SEQUENCENAME = "CONTENT";
	static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("KK:mm:ss a", Locale.ENGLISH);

	public Map<String, EntryResponse> getFormattedResponse(Content content) {
		return content.getEntries().stream()
				.collect(Collectors.toMap(Entry::getContentId,
						entry -> new EntryResponse(entry.getContent(), entry.getLastUpdated().toLocalDate().toString(),
								entry.getLastUpdated().toLocalTime().format(timeFormat), entry.getTitle())));
	}

	private static Predicate<Entry> entryPredicate(String id) {
		return entry -> entry.getContentId().equalsIgnoreCase(id);
	}

	private long getContentSequence(String user) {
		Query query = new Query(Criteria.where("id").is(SEQUENCENAME + "_" + user.toUpperCase()));
		Update update = new Update().inc("seq", 1);
		ContentSequence contentSequence = mongoTemplate.findAndModify(query, update,
				options().returnNew(true).upsert(true), ContentSequence.class);
		return !Objects.isNull(contentSequence) ? contentSequence.getSeq() : 1;
	}

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
	public Content saveContentForDate(SaveContent saveContent, String userName, String contentId) {
		Content content = new Content();
		Optional<Content> existingContent = contentRepo.findContentForUser(userName, saveContent.getContentDate());
		if (existingContent.isEmpty()) {
			int month = LocalDate.parse(saveContent.getContentDate(), dateTimeFormatter).getMonthValue();
			int year = LocalDate.parse(saveContent.getContentDate(), dateTimeFormatter).getYear();
			List<Entry> newContent = new ArrayList<>();
			content.setUserName(userName);
			content.setDate(saveContent.getContentDate());
			content.setMonth(String.valueOf(month));
			content.setYear(String.valueOf(year));
			content.setLastUpdated(LocalDateTime.now());
			newContent.add(new Entry(SEQUENCENAME + getContentSequence(userName), saveContent.getContent(),
					LocalDateTime.now(), saveContent.getTitle()));
			content.setEntries(newContent);
		} else {
			content = existingContent.get();
			List<Entry> existingContentData = content.getEntries();
			if (contentId == null || contentId.equalsIgnoreCase("") || contentId.length() == 0 || contentId.isEmpty()) {
				existingContentData.add(new Entry(SEQUENCENAME + getContentSequence(userName), saveContent.getContent(),
						LocalDateTime.now(), saveContent.getTitle()));
			} else {
				existingContentData.forEach(entry -> {
					if (entry.getContentId().equals(contentId)) {
						entry.setContent(saveContent.getContent());
						entry.setLastUpdated(LocalDateTime.now());
						entry.setTitle(saveContent.getTitle());
					}
				});
			}
			content.setEntries(existingContentData);
			content.setLastUpdated(LocalDateTime.now());
		}
		contentRepo.save(content);
		return content;
	}

	@Override
	public List<String> getDatesForMonth(String userName, String month, String year) {
		Criteria criteria = Criteria.where("userName").is(userName).and("month").is(month).and("year").is(year);
		Query query = new Query(criteria);
		query.fields().include("date");
		return mongoTemplate.find(query, Content.class).stream().map(Content::getDate).toList();
	}

	@Override
	public Map<String, EntryResponse> getDateContent(String userName, String date) {
		Map<String, EntryResponse> contentMap = new HashMap<>();
		Optional<Content> content = contentRepo.findContentForUser(userName, date);
		if (!content.isEmpty()) {
			contentMap = getFormattedResponse(content.get());
		}
		return contentMap;
	}

	@Override
	public Map<String, EntryResponse> removeEntry(String userName, String date, String contentId)
			throws ContentNotFoundException {
		Map<String, EntryResponse> contentMap = new HashMap<>();
		Optional<Content> content = contentRepo.findContentForUser(userName, date);
		if (!content.isEmpty()) {
			boolean contentExists = content.get().getEntries().stream().anyMatch(entryPredicate(contentId));
			if (contentExists) {
				content.get().getEntries().removeIf(entryPredicate(contentId));
				if (!content.get().getEntries().isEmpty()) {
					contentRepo.save(content.get());
					contentMap = getFormattedResponse(content.get());
				} else {
					contentRepo.delete(content.get());
				}
			} else {
				throw new ContentNotFoundException("Please Validate the content Id");
			}
		} else {
			throw new ContentNotFoundException("No Entries found for the given " + date);
		}
		return contentMap;
	}

	@Override
	public Boolean removeAllEntry(String userName, String date) throws ContentNotFoundException {
		Optional<Content> content = contentRepo.findContentForUser(userName, date);
		if (!content.isEmpty()) {
			contentRepo.delete(content.get());
			return true;
		}
		throw new ContentNotFoundException("Content Not available");
	}

}
