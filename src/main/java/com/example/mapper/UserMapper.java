package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //根据学号查用户（学生登录用）
    @Select("select * from user where student_id = #{studentId}")
    User findByStudentId(String studentId);

    //根据手机号查用户（老师登陆用）
    @Select("select * from user where phone = #{phone}")
    User findByPhone(String phone);

    //根据账号查用户（学号或手机号，通用登录）
    @Select("select * from user where student_id = #{account} or phone = #{account}")
    User findByAccount(String account);

    //查所有用户
    @Select("select * from user order by created_at desc")
    List<User> findAllUser();

    //插入新用户
    @Insert("insert into user(student_id,name,password,phone,grade,class_name,department,role)"
            + "values(#{studentId},#{name},#{password},#{phone},#{grade},#{className},#{department},#{role})")
    int insert(User user);

    //修改用户信息
    @Update("update user set name=#{name},phone=#{phone},grade=#{grade},class_name=#{className},department=#{department}"
+"where student_id=#{studentId}")
    int update(User user);

    //删除用户
    @Delete("delete from user where student_id=#{studentId}")
    int deleteById(String studentId);

}

