package com.example.service.Impl;

import com.example.dto.MemberDTO;
import com.example.entity.Activity;
import com.example.entity.ActivityMember;
import com.example.entity.User;
import com.example.mapper.ActivityMapper;
import com.example.mapper.ActivityMemberMapper;
import com.example.service.ActivityService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private ActivityMemberMapper activityMemberMapper;

    @Autowired
    private UserService userService;

    //创建活动，创建者自动成为发起人，并且自动成为成员
    @Override
    @Transactional
    public Long createActivity(String name, String description, String creatorId,
                               LocalDateTime signStartTime, LocalDateTime signEndTime) {

        //插入活动
        Activity activity = new Activity();
        activity.setName(name);
        activity.setDescription(description);
        activity.setCreatorId(creatorId);
        activity.setSignStartTime(signStartTime);
        activity.setSignEndTime(signEndTime);
        activityMapper.insert(activity);

        //创建者自动加入成为成员
        ActivityMember member = new ActivityMember();
        member.setActivityId(activity.getId());
        member.setUserId(creatorId);
        activityMemberMapper.insert(member);
        return activity.getId();
    }

    //添加成员，只有发起人才能操作
    @Override
    @Transactional
    public boolean addMember(Long activityId, String operatorId, String targetId) {

        //校验操作者是不是发起人
        Activity activity = activityMapper.findById(activityId);
        if (activity == null || !activity.getCreatorId().equals(operatorId)) {
            return false;
        }

        //查找目标用户（学号和手机号都能找到）
        User target = userService.findByStudentId(targetId);
        if (target == null) {
            target = userService.findByPhone(targetId);
        }
        if (target == null) {
            return false;
        }

        //检查是否已是成员
        int count = activityMemberMapper.countByActivityAndUser(activityId, target.getStudentId());
        if (count > 0) {
            return false;
        }

        //添加成员
        ActivityMember member = new ActivityMember();
        member.setActivityId(activityId);
        member.setUserId(target.getStudentId());
        activityMemberMapper.insert(member);
        return true;
    }

    //移除成员
    @Override
    @Transactional
    public boolean removeMember (Long activityId,String operatorId,String targetId){
        Activity activity = activityMapper.findById(activityId);
        if (activity == null || !activity.getCreatorId().equals(operatorId)){
            return false;
        }
        if(targetId.equals(operatorId)){
            return false;
        }
        return activityMemberMapper.delete(activityId,targetId) > 0;
    }
    @Override
    public List<Activity> getActivityByCreator(String creatorId){
        return activityMapper.findByCreatorId(creatorId);
    }

    @Override
    public List<Activity> getActivityByMember(String userId){
        return activityMapper.findByMemberId(userId);
    }

    @Override
    public Activity getActivityById(Long activityId){
        return activityMapper.findById(activityId);
    }

    @Override
    public List<String> getMemberIds(Long activityId) {
        return activityMemberMapper.findMemberIdsByActivityId(activityId);
    }

    @Override
    public List<MemberDTO> getMemberNames(Long activityId) {
        return activityMemberMapper.findMemberNameAndId(activityId);
    }

    @Override
    public boolean isMember(Long activityId, String userId) {
        return activityMemberMapper.countByActivityAndUser(activityId, userId) >0;
    }

    @Override
    public boolean isCreator(Long activityId, String userId) {
        Activity activity = activityMapper.findById(activityId);
        return activity != null && activity.getCreatorId().equals(userId);
    }

    @Override
    public List<Activity> findAllActivities() {
        return activityMapper.findAllActivities();
    }

    @Override
    public boolean deleteActivity(Long activityId) {
        return activityMapper.deleteById(activityId) >0;
    }



}
