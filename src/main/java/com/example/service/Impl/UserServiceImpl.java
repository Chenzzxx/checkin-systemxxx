package com.example.service.Impl;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    //学生注册，学号唯一，角色默认普通成员
    @Override
    public boolean registerStudent(User user) {
        if (userMapper.findByStudentId(user.getStudentId()) != null) {
            return false;
        }
        user.setRole(0);
        userMapper.insert(user);
        return true;
    }
    //老师注册，手机号唯一，系统自动生成学号，角色默认普通成员
    public boolean registerTeacher(User user){
        if(userMapper.findByPhone(user.getPhone())!=null){
            return false;
        }
        user.setStudentId("t" + System.currentTimeMillis());
        user.setRole(0);
        userMapper.insert(user);
        return true;
    }

    //登录，支持学号或手机号登录
    @Override
    public User login(String account,String password){
        User user = userMapper.findByAccount(account);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    //根据学号查用户
    @Override
    public User findByStudentId(String studentId){
        return userMapper.findByStudentId(studentId);
    }

    //根据手机号查用户
    @Override
    public User findByPhone(String phone){
        return userMapper.findByPhone(phone);
    }

    //查询所有用户
    @Override
    public List<User> findAllUsers(){
        return userMapper.findAllUser();
    }

    //根据学号删除用户
    @Override
    public boolean deleteById(String studentId){
        return userMapper.deleteById(studentId)>0;
    }

    //修改用户信息
    @Override
    public boolean update(User user){
        return userMapper.update(user)>0;
    }

}
