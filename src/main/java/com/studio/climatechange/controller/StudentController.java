package com.studio.climatechange.controller;


import com.studio.climatechange.dto.StudentDto;
import com.studio.climatechange.services.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class StudentController {
    private StudentServiceImpl studentService;

    @Autowired
    public StudentController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/home")
    public String listStudents(Model model) {
        List<StudentDto> students = studentService.findAllStudents();

        model.addAttribute("students", students);

        return "home";
    }
}
