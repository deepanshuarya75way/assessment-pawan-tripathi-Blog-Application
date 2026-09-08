package com.blogapp.dto;

import com.blogapp.model.Comment;

import java.time.Instant;

public class CommentResponse {
    private String id;
    private String postId;
    private String authorId;
    private String authorName;
    private String content;
    private Instant createdAt;

    public static CommentResponse fromEntity(Comment comment) {
        CommentResponse dto = new CommentResponse();
        dto.id = comment.getId();
        dto.postId = comment.getPostId();
        dto.authorId = comment.getAuthorId();
        dto.authorName = comment.getAuthorName();
        dto.content = comment.getContent();
        dto.createdAt = comment.getCreatedAt();
        return dto;
    }

    public String getId() { return id; }
    public String getPostId() { return postId; }
    public String getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }
}
