package com.blogapp.dto;

import java.time.Instant;

public class ErrorResponse {
    private Instant timestamp = Instant.now();
    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public Instant getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getMessage() { return message; }
}
