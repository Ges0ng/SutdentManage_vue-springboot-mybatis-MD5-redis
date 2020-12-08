package com.nmsl.dao;

import com.nmsl.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentDAO {
    List<Student> selectAll();

    void insert(Student student);

    void deleteById(String id);

    Student selectOne(String id);

    void update(Student student);
}
