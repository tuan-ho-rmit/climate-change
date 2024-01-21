package com.studio.climatechange.services.impl;


import com.studio.climatechange.dto.StudentDto;
import com.studio.climatechange.models.Student;
import com.studio.climatechange.repository.StudentRepository;
import com.studio.climatechange.services.StudentServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentServiceInterface {
    private StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


     @Override
    public List<StudentDto> findAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream().map(student -> mapToStudenDto(student)).collect(Collectors.toList());
    }

    @Override
    public StudentDto findBySId() {
        return null;
    }

    @Override
    public StudentDto findByName() {
        return null;
    }

    private StudentDto mapToStudenDto (Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setSId(student.getSId());
        studentDto.setName(student.getName());
        return studentDto;
    }
}
