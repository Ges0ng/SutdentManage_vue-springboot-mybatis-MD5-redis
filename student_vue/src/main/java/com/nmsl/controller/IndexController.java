package com.nmsl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@CrossOrigin    //允许跨域
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "login";
    }

    @RequestMapping("/student")
    public String login(){
        return "studentlist";
    }

    @RequestMapping("/regist")
    public String regist(){
        return "regist";
    }

    @RequestMapping("/add")
    public String add(){
        return "addStudent";
    }

    @RequestMapping("/update")
    public String update(){
        return "updateStudent";
    }
}
