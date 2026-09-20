package com.example.service;

import com.example.entity.SignRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SignService {

    String signIn(Long activityId, String userId);

    List<SignRecord> getRecordsByUser(String userId);

    List<SignRecord> getRecordsByActivity(Long activityId);

    int countByActivityAndUser (Long activityId, String userId);
}
