package com.nmsl.dao;

import com.nmsl.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDAO {
    void save(User user);
    User selectByUserName(String username);

}
