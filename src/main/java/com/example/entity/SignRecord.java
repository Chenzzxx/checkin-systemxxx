package com.example.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SignRecord {
    private Long id;
    private Long activityId;
    private String userId;
    private LocalDateTime signInTime;
    private LocalDateTime createdAt;
}
