package com.blogapp.dto;

import com.blogapp.model.Post;
import com.blogapp.model.PostStatus;

import java.time.Instant;
import java.util.List;

public class PostResponse {
    private String id;
    private String title;
    private String content;
    private String excerpt;
    private String authorId;
    private String authorName;
    private List<String> tags;
    private PostStatus status;
    private int likeCount;
    private Instant createdAt;
    private Instant updatedAt;

    public static PostResponse fromEntity(Post post) {
        PostResponse dto = new PostResponse();
        dto.id = post.getId();
        dto.title = post.getTitle();
        dto.content = post.getContent();
        dto.excerpt = post.getExcerpt();
        dto.authorId = post.getAuthorId();
        dto.authorName = post.getAuthorName();
        dto.tags = post.getTags();
        dto.status = post.getStatus();
        dto.likeCount = post.getLikeCount();
        dto.createdAt = post.getCreatedAt();
        dto.updatedAt = post.getUpdatedAt();
        return dto;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getExcerpt() { return excerpt; }
    public String getAuthorId() { return authorId; }
    public String getAuthorName() { return authorName; }
    public List<String> getTags() { return tags; }
    public PostStatus getStatus() { return status; }
    public int getLikeCount() { return likeCount; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
