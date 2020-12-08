package com.nmsl.service;

import com.nmsl.entity.Student;

import java.util.List;

/**
 * @author 2020
 */
public interface StudentService {
    List<Student> selectAll();

    void insert(Student student);

    void deleteById(String id);

    Student selectOne(String id);

    void update(Student student);
}
