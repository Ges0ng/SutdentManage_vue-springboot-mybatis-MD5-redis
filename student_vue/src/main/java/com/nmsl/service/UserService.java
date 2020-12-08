package com.nmsl.service;

import com.nmsl.entity.User;

/**
 * @author 2020
 */
public interface UserService {

    void register(User user);   //用户注册

    User login(User user);  //用户登录
}
