package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Activity {
    private Long id;
    private String name;
    private String description;
    private String creatorId;
    private LocalDateTime signStartTime;
    private LocalDateTime signEndTime;
    private LocalDateTime createdAt;
}
