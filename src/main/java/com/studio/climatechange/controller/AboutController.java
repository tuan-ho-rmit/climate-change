package com.studio.climatechange.controller;


import com.studio.climatechange.dto.PersonaDto;
import com.studio.climatechange.dto.StudentDto;
import com.studio.climatechange.services.impl.PersonaServiceImpl;
import com.studio.climatechange.services.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AboutController {
    private PersonaServiceImpl personaService;
    private StudentServiceImpl studentService;

    @Autowired
    public AboutController(StudentServiceImpl studentService, PersonaServiceImpl personaService) {
        this.studentService = studentService;
        this.personaService = personaService;
    }


    @GetMapping(value = {"/about-us"})
    public String listPersonas(Model model) {
        List<PersonaDto> personas = personaService.findAllPersonas();
        List<StudentDto> students = studentService.findAllStudents();

        model.addAttribute("personas", personas);
        model.addAttribute("students", students);

        return "about";
    }
}
