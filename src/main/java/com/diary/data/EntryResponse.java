package com.diary.data;

public class EntryResponse {
	private String content;
	private String date;
	private String lastUpdated;
	private String title;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public EntryResponse(String content, String date, String lastUpdated, String title) {
		this.content = content;
		this.date = date;
		this.lastUpdated = lastUpdated;
		this.title = title;
	}

	public EntryResponse() {
	}
}
