package com.diary.data;

public class SaveContent {
	private String contentDate;
	private String content;

	public String getContentDate() {
		return contentDate;
	}

	public void setContentDate(String contentDate) {
		this.contentDate = contentDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public SaveContent(String contentDate, String content) {
		this.contentDate = contentDate;
		this.content = content;
	}

	public SaveContent() {
	}

}
