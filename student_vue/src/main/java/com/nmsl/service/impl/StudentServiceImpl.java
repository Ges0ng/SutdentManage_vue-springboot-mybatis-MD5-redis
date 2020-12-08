package com.nmsl.service.impl;

import com.nmsl.dao.StudentDAO;
import com.nmsl.entity.Student;
import com.nmsl.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 2020
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentDAO studentDAO;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Student> selectAll() {
        return studentDAO.selectAll();
    }

    @Override
    public void insert(Student student) {
        studentDAO.insert(student);
    }

    @Override
    public void deleteById(String id) {
        studentDAO.deleteById(id);
    }

    @Override
    public Student selectOne(String id) {

        return studentDAO.selectOne(id);
    }

    @Override
    public void update(Student student) {
        studentDAO.update(student);
    }
}
