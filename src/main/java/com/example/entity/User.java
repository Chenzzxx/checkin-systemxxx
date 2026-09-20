package com.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private String studentId;
    private String name;
    private String password;
    private String phone;
    private String grade;
    private String className;
    private String department;
    private Integer role;
    private LocalDateTime createdAt;
}
