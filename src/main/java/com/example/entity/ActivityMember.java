package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityMember {
    private Long id;
    private Long activityId;
    private String userId;
    private LocalDateTime joinedAt;

}
