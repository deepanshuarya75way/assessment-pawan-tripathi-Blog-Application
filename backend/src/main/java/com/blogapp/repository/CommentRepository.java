package com.blogapp.repository;

import com.blogapp.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostIdOrderByCreatedAtAsc(String postId);
    void deleteByPostId(String postId);
    long countByPostId(String postId);
}
