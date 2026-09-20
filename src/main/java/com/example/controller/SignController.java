package com.example.controller;

import com.example.dto.ResultDTO;
import com.example.entity.SignRecord;
import com.example.entity.User;
import com.example.service.ActivityService;
import com.example.service.SignService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sign")
public class SignController {

    @Autowired
    private SignService signService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    //签到
    @PostMapping("/in")
    public ResultDTO<String> signIn(@RequestParam Long activityId, @RequestParam String userId){
        User user = userService.findByStudentId(userId);
        if(user == null){
            return ResultDTO.error("用户不存在");
        }
        String result = signService.signIn(activityId,userId);
        if("签到成功".equals(result)){
            return ResultDTO.success(result, null);
        }else if(result.contains("签到尚未开始")||result.contains("签到已结束")){
            return ResultDTO.error(403, result);
        }else{
           return ResultDTO.error(result);
        }
    }

    //个人签到记录
    @GetMapping("/records/{userId}")
    public ResultDTO<List<SignRecord>> getMyRecords(@PathVariable String userId){
        User user = userService.findByStudentId(userId);
        if(user == null){
            return ResultDTO.error("用户不存在");
        }
        return ResultDTO.success(signService.getRecordsByUser(userId));
    }

    //个人签到统计
    @GetMapping("/stats/{userId}")
    public ResultDTO<Map<String, Object>> getMyStats(@PathVariable String userId){
        User user = userService.findByStudentId(userId);
        if(user == null){
            return ResultDTO.error("用户不存在");
        }
        List<SignRecord> records = signService.getRecordsByUser(userId);
        Map<String, Object> stats = new HashMap<>();
        stats.put("userId",userId);
        stats.put("totalCount",records.size());
        return ResultDTO.success(stats);
    }

    //活动签到情况
    @GetMapping("/activity/{activityId}")
    public ResultDTO<Map<String,Object>> getActivitySignRecords(@PathVariable Long activityId,@PathVariable String viewerId){
        if(!activityService.isCreator(activityId,viewerId))
        {
            User viewer = userService.findByStudentId(viewerId);
            if(viewer == null || viewer.getRole() != 2){
                return ResultDTO.error(403,"权限不足，仅发起人或管理员可查看");
            }
        }
        List<String> memberIds = activityService.getMemberIds(activityId);
        List<SignRecord> records = signService.getRecordsByActivity(activityId);
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> memberStatus = new HashMap<>();
        for(String memberId : memberIds){
            User member = userService.findByStudentId(memberId);
            String memberName = member != null ? member.getName():memberId;
            long count  = records.stream().filter(r -> r.getUserId().equals(memberId))
                    .count();
            Map<String,Object> status = new HashMap<>();
            status.put("name",memberId);
            status.put("count",count);
            status.put("signed",count >0);
            memberStatus.put(memberId,status);
        }
        result.put("activityId",activityId);
        result.put("totalMembers",memberIds.size());
        result.put("totalSigned",records.stream().map(SignRecord::getUserId).distinct().count());
        result.put("member",memberStatus);
        result.put("records",records);
        return ResultDTO.success(result);
    }
}
