package com.nmsl.service.impl;

import com.nmsl.dao.UserDAO;
import com.nmsl.entity.User;
import com.nmsl.service.UserService;
import com.nmsl.utils.Md5Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 2020
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;

    @Override
    public void register(User user) {
        //0.根据用户输入的用户名判断用户是否存在
        User userExists = userDAO.selectByUserName(user.getUsername());
        if (userExists == null) {
            //1.生成用户状态        //2.设置用户的注册时间
            user.setStatus("已激活").setRegisterTime(new Date())
                    .setPassword(Md5Utils.string2Md5(user.getPassword()));

            //3.调用dao
            userDAO.save(user);
        }else {
            throw new RuntimeException("用户已存在!");
        }




    }

    @Override
    public User login(User user) {
        //1.根据用户输入的用户名进行查询
        User userDB = userDAO.selectByUserName(user.getUsername());
        if (!ObjectUtils.isEmpty(userDB)) {
            //2.比较密码
            String Md5pwd = Md5Utils.string2Md5(user.getPassword());
            if (userDB.getPassword().equals(Md5pwd)){
                return userDB;
            }else {
                throw new RuntimeException("密码输入不正确");
            }
        }else {
            throw new RuntimeException("用户名错误");
        }
    }

}
