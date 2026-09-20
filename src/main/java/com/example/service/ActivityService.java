package com.example.service;


import com.example.dto.MemberDTO;
import com.example.entity.Activity;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityService {

    Long createActivity(String name, String description, String creatorId,
                        LocalDateTime signStartTime, LocalDateTime signEndTime);

    boolean addMember(Long activityId, String operatorId, String targetId);

    boolean removeMember(Long activityId, String operatorId, String targetId);

    List<Activity> getActivityByCreator(String creatorId);

    List<Activity> getActivityByMember(String userId);

    Activity getActivityById(Long activityId);

    List<String> getMemberIds(Long activityId);

    List<MemberDTO> getMemberNames(Long activityId);

    boolean isMember(Long activityId,String userId);

    boolean isCreator(Long activityId, String userId);

    List<Activity> findAllActivities();

    boolean deleteActivity(Long activityId);


}
