package com.blogapp.repository;

import com.blogapp.model.Post;
import com.blogapp.model.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {

    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    Page<Post> findByAuthorId(String authorId, Pageable pageable);

    Page<Post> findByStatusAndTitleContainingIgnoreCase(PostStatus status, String title, Pageable pageable);

    Page<Post> findByStatusAndTagsContainingIgnoreCase(PostStatus status, String tag, Pageable pageable);
}

