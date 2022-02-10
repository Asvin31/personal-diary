package com.diary.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.diary.model.UserDao;

public interface UserRepo extends MongoRepository<UserDao, String> {
	
	@Query("{userName : ?0}")
	UserDao findByUserName(String userName);
}
