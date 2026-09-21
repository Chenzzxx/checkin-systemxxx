package com.example.controller;


import com.example.dto.MemberDTO;
import com.example.dto.ResultDTO;
import com.example.entity.Activity;
import com.example.entity.User;
import com.example.service.ActivityService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    //创建活动
    @PostMapping("/create")
    public ResultDTO<Long> createActivity(@RequestParam String creatorId, @RequestParam String name,
                                          @RequestParam(required = false)String description,@RequestParam String signStartTime,
                                          @RequestParam String signEndTime){
        User creator = userService.findByStudentId(creatorId);
        if(creator == null){
            return ResultDTO.error("用户不存在");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime;
        LocalDateTime endTime;
        try{
            startTime = LocalDateTime.parse(signStartTime,formatter);
            endTime = LocalDateTime.parse(signEndTime,formatter);
        }catch (Exception e){
            return ResultDTO.error("时间格式错误，请使用：yyyy-MM-dd HH:mm:ss");
        }
        if(startTime.isAfter(endTime)){
            return ResultDTO.error("签到开始时间不能晚于结束时间");
        }
        Long activityId = activityService.createActivity(creatorId,name,description,startTime,endTime);
        return ResultDTO.success("创建成功",activityId);
    }

    //添加成员（发起人）
    @PostMapping("/addMember")
    public ResultDTO<String> addMember(@RequestParam Long activityId,@RequestParam String operatorId,
                                       @RequestParam String targetId){
        boolean success = activityService.addMember(activityId,operatorId,targetId);
        if(!success){
            return ResultDTO.error("添加失败，请检查权限或用户是否已是成员");
        }
        return ResultDTO.success("添加成功",null);
    }

    //移除成员（发起人）
    @DeleteMapping("/removeMember")
    public ResultDTO<String> removeMember(@RequestParam Long activityId, @RequestParam String operatorId,
                                          @RequestParam String targetId){
        boolean success = activityService.removeMember(activityId, operatorId, targetId);
        if(!success){
            return ResultDTO.error("移除失败，请检查权限");
        }
        return ResultDTO.success("移除成功",null);
    }

    //我发起的活动
    @GetMapping("/my/created")
    public ResultDTO<List<Activity>> getMyCreatedActivities(@RequestParam String userId)
    {
        return ResultDTO.success(activityService.getActivityByCreator(userId));
    }

    //我参与的活动
    @GetMapping("/my/joined")
    public ResultDTO<List<Activity>> getMyJoinedActivities(@RequestParam String userId){
        return ResultDTO.success(activityService.getActivityByMember(userId));
    }

    //活动成员列表
    @GetMapping("/{activityId}/members")
    public ResultDTO<List<MemberDTO>> getActivityMembers(@PathVariable Long activityId,@RequestParam String viewerId)
    {
        if (!activityService.isCreator(activityId,viewerId)){
            User viewer = userService.findByStudentId(viewerId);
            if(viewer == null || viewer.getRole() != 2){
                return ResultDTO.error(403,"权限不足");
            }
        }
        return ResultDTO.success(activityService.getMemberNames(activityId));
    }
}



























































