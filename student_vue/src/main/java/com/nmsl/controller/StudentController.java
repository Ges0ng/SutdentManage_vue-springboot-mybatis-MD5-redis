package com.nmsl.controller;

import com.nmsl.entity.Student;
import com.nmsl.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 2020
 */
@RestController
@RequestMapping("/student")
@CrossOrigin
@Slf4j
public class StudentController {

    @Resource
    private StudentService studentService;

    @Value("${photo.dir}")
    private String realPath;

    //添加学生信息
    @PostMapping("/add")
    public Map<String, Object> addStudent(Student student, MultipartFile photo) throws IOException {
        log.info("学生信息:[{}]",student.toString());
        log.info("学生头像:[{}]",photo.getOriginalFilename());

        Map<String, Object> map = new HashMap<>();
        try {
            //头像如何保存?
            String newFileName = UUID.randomUUID().toString()
                    + "." + FilenameUtils.getExtension(photo.getOriginalFilename());
            photo.transferTo(new File(realPath,newFileName));
            //设置头像地址
            student.setPhotopath(newFileName);
            //保存信息
            studentService.insert(student);
            map.put("state",true);
            map.put("msg","学生信息保存成功");

        } catch (IOException e) {
            e.printStackTrace();
            map.put("state",false);
            map.put("msg","学生信息保存失败");
        }
        return map;
    }


    //获取学生列表
    @RequestMapping("/selectAll")
    public List<Student> selectAll(){
        return studentService.selectAll();
    }

    //删除学生
    @GetMapping("/delete")
    public Map<String, Object> delete(String id){
        log.info("删除员工的id:[{}]",id);
        Map<String, Object> map = new HashMap<>();
        try {
            //删除学生信息包括头像
            Student student = studentService.selectOne(id);
            File file = new File(realPath, student.getPhotopath());
            if (file.exists()){
                file.delete();  //删除头像
            }

            studentService.deleteById(id);

            map.put("state",true);
            map.put("msg","删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state",false);
            map.put("msg","删除失败!");
        }

        return map;
    }

    //查询一个学生
    @GetMapping("/selectOne")
    public Student selectOne(String id){
        log.info("查询学生信息的id",id);
        return studentService.selectOne(id);
    }


    //修改学生信息
    @PostMapping("/update")
    public Map<String, Object> update(Student student, MultipartFile photo) throws IOException {
        log.info("学生信息:[{}]",student.toString());
        if (photo != null && photo.getSize() != 0) {
            log.info("学生头像:[{}]",photo.getOriginalFilename());
        }
        Map<String, Object> map = new HashMap<>();
        try {
            String.valueOf(photo);
            if (photo != null && photo.getSize() != 0   && !photo.isEmpty()
                    && !"".equals(photo.toString().trim())
                        && !"".equals(photo)  ) {

                //bug:如果有一次修改的时候没有改动头像,数据库的photo属性被清空了
                // 下一次改动头像的时候会报空指针异常.
                if (student.getPhotopath() != null) {
                    //在本地删除学生的旧的头像
                    student = studentService.selectOne(student.getId());
                    File file = new File(realPath, student.getPhotopath());
                    if (file.exists()) {
                        file.delete();  //删除头像
                    }
                }


                //更换新头像
                String newFileName = UUID.randomUUID().toString()
                        + "." + FilenameUtils.getExtension(photo.getOriginalFilename());
                photo.transferTo(new File(realPath,newFileName));
                //设置头像地址
                student.setPhotopath(newFileName);

            }
            //保存信息
            studentService.update(student);
            map.put("state",true);
            map.put("msg","学生信息保存成功");

        } catch (IOException e) {
            e.printStackTrace();
            map.put("state",false);
            map.put("msg","学生信息保存失败");
        }
        return map;
    }
}
