package com.example.dto;

import lombok.Data;

@Data
public class StudentRegisterDTO {
    private String studentId;
    private String name;
    private String password;
    private String grade;
    private String className;
    private String department;
}
