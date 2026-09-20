package com.example.mapper;

import com.example.dto.MemberDTO;
import com.example.entity.ActivityMember;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ActivityMemberMapper {

    //添加（插入）成员
    @Insert("insert into activity_member (activity_id,user_id) values (#{activityId},#{userId})")
    int insert (ActivityMember member);

    //查询活动成员（姓名+学号）
    @Select("select u.student_id,u.name from activity_member am join user u on am.user_id = u.student_id"
                + "where am.activity_id=#{ActivityId}")
    List<MemberDTO> findMerberNameAndId(Long activityId);

    //查活动所有成员记录
    @Select("select * from activity_member where activity_id = #{activityId}")
    List<ActivityMember> findByActivityId(Long activityId);

    //判断是否是某活动成员
    @Select("select count(*) from activity_member where activity_id = #{activityId} and user_id = #{userId}")
    int countByActivityAndUser(Long activityId,String userId);

    //删除成员
    @Delete("delete from activity_member where activity_id = #{activityId} and user_id =#{userId}")
    int delete(Long activityId,String userId);
}
