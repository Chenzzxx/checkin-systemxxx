package com.example.mapper;

import com.example.entity.Activity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ActivityMapper {

    //创建(插入）活动
    @Insert("insert into activity (name,description,creator_id,sign_start_time,sign_end_time)"
                      + "values(#{name},#{description},#{creatorId},#{signStartTime},#{signEndTime})")
    int insert(Activity activity);

    //根据Id查活动
    @Select("select * from activity where id=#{id}")
    Activity findById(Long id);

    //查看我发起的活动
    @Select("select * from activity where creator_id=#{createrId}")
    List<Activity> findByCreatorId(@Param("creatorId")String creatorId);

    //查看我参与的活动
    @Select("select a.* from activity a join activity_member am on a.id = am.activity_id where am.user_id =#{userId}")
    List<Activity> findByMemberId(@Param("userId")String userId);

    //查看所有活动
    @Select("select * from activity order by created_at desc")
    List<Activity> findAllActivities();


    //删除活动
    @Delete("delete from activity where id=#{id}")
    int deleteById(Long id);

}
