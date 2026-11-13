package com.example.dto;

import lombok.Data;

@Data
public class MemberDTO {
    private String studentId;
    private String name;

    private Integer signedCount;
    private Boolean signedToday;
}
