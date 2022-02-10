package com.diary.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.diary.model.Content;

public interface ContentRepo extends MongoRepository<Content, String> {

	@Query("{userName :?0 , date: ?1}")
	Optional<Content> findContentForUser(String userName, String date);
}
