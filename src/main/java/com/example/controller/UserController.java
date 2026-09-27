package com.example.controller;

import com.example.dto.LoginRequestDTO;
import com.example.dto.ResultDTO;
import com.example.dto.StudentRegisterDTO;
import com.example.dto.TeacherRegisterDTO;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    //学生注册
    @PostMapping("/register/student")
    public ResultDTO<String> registerStudent(@RequestBody StudentRegisterDTO dto) {
        User user = new User();
        user.setStudentId(dto.getStudentId());
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());
        user.setGrade(dto.getGrade());
        user.setClassName(dto.getClassName());
        user.setDepartment(dto.getDepartment());
        boolean success = userService.registerStudent(user);
        if (success) {
            return ResultDTO.success("注册成功", null);
        }
        return ResultDTO.error("学号已存在");
    }

    //老师注册
    @PostMapping("/register/teacher")
    public ResultDTO<String> registerTeacher(@RequestBody TeacherRegisterDTO dto) {
        User user = new User();
        user.setPhone(dto.getPhone());
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());
        user.setDepartment(dto.getDepartment());
        boolean success = userService.registerTeacher(user);
        if (!success) {
            return ResultDTO.error("手机号已注册");
        }
        User saved = userService.findByPhone(dto.getPhone());
        return ResultDTO.success("注册成功，您的账号是：" + saved.getStudentId(), saved.getStudentId());
    }

    //登录
    @PostMapping("/login")
    public ResultDTO<User> login(@RequestBody LoginRequestDTO request) {
        User user = userService.login(request.getAccount(), request.getPassword());
        if (user == null) {
            return ResultDTO.error("账号或密码错误");
        }
        user.setPassword(null);
        return ResultDTO.success(user);
    }

    //查询所有用户（管理员）
    @GetMapping("/list")
    public ResultDTO<List<User>> listAllUsers(@RequestParam String adminId) {
        User admin = userService.findByStudentId(adminId);
        if (admin == null || admin.getRole() != 2) {
            return ResultDTO.error(403, "权限不足");
        }
        List<User> users = userService.findAllUsers();
        users.forEach(u -> u.setPassword(null));
        return ResultDTO.success(users);
    }

    //删除用户
    @DeleteMapping("/{studentId}")
    public ResultDTO<String> deleteUser(@PathVariable String studentId, @RequestParam String adminId) {
        User admin = userService.findByStudentId(adminId);
        if (admin == null || admin.getRole() != 2) {
            return ResultDTO.error(403, "权限不足");
        }
        boolean success = userService.deleteById(studentId);
        if (success) {
            return ResultDTO.success("删除成功", null);
        }
        return ResultDTO.error("用户不存在");
    }

    //修改用户信息（管理员）
    @PutMapping("/{studentId}")
    public ResultDTO<String> updateUser(@PathVariable String studentId, @RequestBody User user, @RequestParam String adminId) {
        User admin = userService.findByStudentId(adminId);
        if (admin == null || admin.getRole() != 2) {
            return ResultDTO.error(403, "权限不足");
        }
        user.setStudentId(studentId);
        boolean success = userService.update(user);
        if (success) {
            return ResultDTO.success("更新成功", null);
        }
        return ResultDTO.error("用户不存在");
    }
}

