package com.blogapp.dto;

import java.time.Instant;
import java.util.List;

public class UserProfileResponse {
    private String id;
    private String name;
    private String email;
    private String bio;
    private List<String> roles;
    private Instant createdAt;

    public UserProfileResponse(String id, String name, String email, String bio, List<String> roles, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getBio() { return bio; }
    public List<String> getRoles() { return roles; }
    public Instant getCreatedAt() { return createdAt; }
}
