package com.example.service.Impl;

import com.example.entity.Activity;
import com.example.entity.SignRecord;
import com.example.mapper.SignRecordMapper;
import com.example.service.ActivityService;
import com.example.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SignServiceImpl implements SignService {

    @Autowired
    private SignRecordMapper signRecordMapper;

    @Autowired
    private ActivityService activityService;

    @Override
    public String signIn(Long activityId, String uersId){

        //检查用户是否是活动成员
        if (!activityService.isMember(activityId,userId)){
            return "您不是该活动的成员，无法签到";
        }

        //检查活动是否存在
        Activity activity = activityService.getActivityById(activityId);
        if(activity == null){
            return "活动不存在";
        }

        //校验签到时间
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(activity.getSignStartTime())){
            return "签到尚未开始，开始时间：" + activity.getSignStartTime();
        }
        if(now.isAfter(activity.getSignEndTime())){
            return "签到已结束，结束时间：" + activity .getSignEndTime();
        }

        //检查今日是否已签到
        int count = signRecordMapper.countTodayByUserAndActivity(actvityId,userId);
        if(count>0){
            return "今日已签到，请勿重复签到";
        }

        //执行签到
        SignRecord record = new SignRecord();
        record.setActivityId(activityId);
        record.setUserId(userId);
        record.setSignInTime(now);
        signRecordMapper.insert(record);
        return "签到成功";
    }

    @Override
    public List<SignRecord> getRecordsByUser(String userId){
        return signRecordMapper.findByUserId(userId);
    }

    @Override
    public List<SignRecord> getRecordsByActivity(Long activityId){
        return signRecordMapper.findByActivityId(activityId);
    }

    @Override
    public int countByActivityAndUser(Long activityId, String userId){
        return signRecordMapper.countByActivityAndUser(activityId,userId);
    }


}
