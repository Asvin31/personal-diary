package com.diary.data;

import java.util.Map;

public class ContentResponse {
	private Map<String, EntryResponse> content;
	private ErrorData errorData;

	public Map<String, EntryResponse> getContent() {
		return content;
	}

	public void setContent(Map<String, EntryResponse> content) {
		this.content = content;
	}

	public ErrorData getErrorData() {
		return errorData;
	}

	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	public ContentResponse(Map<String, EntryResponse> content, ErrorData errorData) {
		this.content = content;
		this.errorData = errorData;
	}

}
