package com.diary.model;

import java.time.LocalDateTime;

public class Entry {
	private String contentId;
	private String content;
	private LocalDateTime lastUpdated;
	private String title;

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Entry(String contentId, String content, LocalDateTime lastUpdated, String title) {
		this.contentId = contentId;
		this.content = content;
		this.lastUpdated = lastUpdated;
		this.title = title;
	}

	public Entry() {
	}

}
