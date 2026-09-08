package com.blogapp.dto;

import com.blogapp.model.PostStatus;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class PostRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private String excerpt;

    private List<String> tags;

    private PostStatus status = PostStatus.PUBLISHED;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getExcerpt() { return excerpt; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public PostStatus getStatus() { return status; }
    public void setStatus(PostStatus status) { this.status = status; }
}
