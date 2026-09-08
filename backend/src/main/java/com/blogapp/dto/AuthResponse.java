package com.blogapp.dto;

import java.util.List;

public class AuthResponse {
    private String token;
    private String id;
    private String name;
    private String email;
    private List<String> roles;

    public AuthResponse(String token, String id, String name, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() { return token; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getRoles() { return roles; }
}
