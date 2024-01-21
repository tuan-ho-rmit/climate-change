package com.studio.climatechange.services;

import com.studio.climatechange.dto.StudentDto;

import java.util.List;

public interface StudentServiceInterface {
    List<StudentDto> findAllStudents();
    StudentDto findBySId();
    StudentDto findByName();

}
