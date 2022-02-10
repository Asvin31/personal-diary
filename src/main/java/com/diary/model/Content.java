package com.diary.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Content")
public class Content {
	@Id
	private String id;
	private String userName;
	private String date;
	private String month;
	private String year;
	private LocalDateTime lastUpdated;

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	private List<String> content;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	public Content(String userName, String date, String month, String year, LocalDateTime lastUpdated,
			List<String> content) {
		this.userName = userName;
		this.date = date;
		this.month = month;
		this.year = year;
		this.lastUpdated = lastUpdated;
		this.content = content;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Content() {
	}

}
