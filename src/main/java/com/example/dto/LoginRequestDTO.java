package com.example.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String account; //学号或手机号
    private String password;
}
