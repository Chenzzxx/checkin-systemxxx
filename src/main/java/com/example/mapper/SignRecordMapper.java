package com.example.mapper;

import com.example.entity.SignRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SignRecordMapper {


    //插入签到记录
    @Insert("insert into sign_record (activity_id,user_id,sign_in_time)values(#{activityId},#{userId},#{signInTime})")
    int insert(SignRecord record);

    //查看活动签到情况
    @Select("select * from sign_record where activity_id = #{activityId} order by sign_in_time desc")
    List<SignRecord> findByActivityId(Long activityId);

    //今日是否已签到
    @Select("select count(*) from sign_record where activity_id = #{activityId} and user_id = #{UserId} and date(sign_in_time) = curdate()")
    int countTodayByUserAndActivity(Long activityId, String userId);

    //查看个人所有签到记录
    @Select("select * from sign_record where user_id = #{UserId} order by sign_in_time desc")
    List<SignRecord> findByUserId(String userId);

    //统计总签到次数
    @Select("select count(*) from sign_record where activity_id = #{activityId} and user_id = #{userId}")
    int countByActivityAndUser(Long activityId,String userId);

}

