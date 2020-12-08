package com.nmsl.controller;

import com.nmsl.entity.User;
import com.nmsl.service.UserService;
import com.nmsl.utils.VerifyCodeUtils;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 2020
 */
@RestController
@CrossOrigin    //允许跨域
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;




    /**
     * 生成验证码图片
     */
    @GetMapping("/getImage")
    public String getImageCode(HttpServletRequest request) throws IOException {
        //1.使用工具类生成验证码. 4位数的验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //2.拿去和用户输入的验证码做比对,所以要存入到servletContext(最大的作用域).日后可以存入到redis中
        request.getServletContext().setAttribute("code",code);

        //3.将图片转为base64处理.

        //创建一个内存中的输出流
        ByteOutputStream byteOutputStream = new ByteOutputStream();
        VerifyCodeUtils.outputImage(100,30,byteOutputStream,code);
        String base64 = "data:image/png;base64," + Base64Utils.encodeToString(byteOutputStream.toByteArray());
        return base64;
    }


    /**
     * 用来处理用户注册的方法
     */

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user,String code,HttpServletRequest request){     //直接封装对象要加requestbody,传数据是直接传json字符串
        log.info("用户信息[{}]",user.toString());
        log.info("验证码信息[{}]",code);

        Map<String, Object> map =new HashMap<>();
        //1.调用业务对象
        try {
                //得到用户输入的验证码
            String key = (String) request.getServletContext().getAttribute("code");
                //比对验证码是否正确
            if (key.equalsIgnoreCase(code.trim())){
                userService.register(user);
                map.put("state", true);
                map.put("msg", "提示: 注册成功");
            }else {
                throw new RuntimeException("验证码错误");
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", "提示: 注册失败,"+e.getMessage());
        }


        return map; 
    }


    /**
     * 用户登录的方法
     */

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user){
        log.info("当前登录用户的信息:[{}]",user.toString());
        Map<String, Object> map = new HashMap<>();

        try {
            User userDB = userService.login(user);
            map.put("state",true);
            map.put("msg","登陆成功!");
            map.put("user", userDB);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state",false);
            map.put("msg",e.getMessage());
        }
        return map;
    }

}
