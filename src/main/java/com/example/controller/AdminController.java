package com.example.controller;


import com.example.dto.ResultDTO;
import com.example.entity.Activity;
import com.example.entity.User;
import com.example.service.ActivityService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    //判断是不是管理员
    private boolean isAdmin(String userId){
        User user = userService.findByStudentId(userId);
        return user != null && user.getRole() == 2;
    }

    //查看所有用户
    @GetMapping("/users")
    public ResultDTO<List<User>> getAllUsers(@RequestParam String adminId){
        if(!isAdmin(adminId)){
            return ResultDTO.error(403,"权限不足");
        }
        List<User> users = userService.findAllUsers();
        users.forEach(u -> u.setPassword(null));
        return ResultDTO.success(users);
    }

    //查看所有活动
    @GetMapping("/activities")
    public ResultDTO<List<Activity>> getAllActivities(@RequestParam String adminId){
        if(!isAdmin(adminId)){
            return ResultDTO.error(403,"权限不足");
        }
        return ResultDTO.success(activityService.findAllActivities());
    }

    //删除活动
    @DeleteMapping("/activity/{activityId}")
    public ResultDTO<String> deleteActivity(@PathVariable Long activityId, @RequestParam String adminId){
        if(!isAdmin((adminId)){
            return  ResultDTO.error(403,"权限不足");
        }
        boolean success = activityService.deleteActivity(activityId);
        if(success){
            return ResultDTO.success("删除成功",null);
        }
        return ResultDTO.error("活动不存在");
    }
}
