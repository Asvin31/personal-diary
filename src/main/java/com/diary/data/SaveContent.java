package com.diary.data;

public class SaveContent {
	private String contentDate;
	private String content;
	private String contentId;
	private String title;

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

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public SaveContent(String contentDate, String content, String contentId, String title) {
		this.contentDate = contentDate;
		this.content = content;
		this.contentId = contentId;
		this.title = title;
	}

	public SaveContent() {
	}

}
