package com.example.service;

import com.example.entity.User;

import java.util.List;

public interface UserService {

    boolean registerStudent(User user);

    boolean registerTeacher(User user);

    User login(String account, String password);

    User findByStudentId(String studentId);

    User findByPhone(String phone);

    List<User> findAllUsers();

    boolean delectById(String studentId);

    boolean update(User user);

}
