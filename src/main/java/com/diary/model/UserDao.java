package com.diary.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class UserDao {
	@Id
	private String id;
	private String userName;
	private String password;
	private List<String> roles;

	public UserDao() {
	}

	public UserDao(String userName, String password, List<String> roles) {
		this.userName = userName;
		this.password = password;
		this.roles = roles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserDao [id=" + id + ", userName=" + userName + ", password=" + password + ", roles=" + roles + "]";
	}
}
