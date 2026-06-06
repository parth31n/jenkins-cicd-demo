package com.demo.model;

public class ApiResponse {
    private String message;
    private String status;
    private long timestamp;

    public ApiResponse(String message, String status) {
        this.message = message;
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }

    public String getMessage() { return message; }
    public String getStatus() { return status; }
    public long getTimestamp() { return timestamp; }
}
