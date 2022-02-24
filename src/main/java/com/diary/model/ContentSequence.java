package com.diary.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "content_seq")
public class ContentSequence {

	private String id;
	private long seq;
	private String user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ContentSequence(String id, long seq, String user) {
		this.id = id;
		this.seq = seq;
		this.user = user;
	}

	public ContentSequence() {
	}

}
