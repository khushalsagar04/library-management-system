package com.example.demo.controller;

import com.example.demo.dto.CreateStudentRequest;
import com.example.demo.dto.UpdateStudentRequest;
import com.example.demo.models.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {
    @Autowired
    StudentService studentService;

//    create Student
    @PostMapping("/student")
    public void createStudent(@RequestBody @Valid CreateStudentRequest createStudentRequest){
        studentService.createStudent(createStudentRequest.toStudent());
    }

//    find Student by Id
    @GetMapping("/student/{id}")
    public List<Student> getStudentById(@PathVariable("id") Integer id){
        return studentService.findStudentById(id);
    }
//    find Student by Email
    @GetMapping("/studentByEmail")
    public List<Student> getStudentByEmail(@RequestParam("email") String email){
        return studentService.findStudentByEmail(email);
    }

//    find Student by Roll Number
//    find Student by Name
//    @GetMapping("/student/{name}")
//    public List<Student> getStudentByName(@PathVariable("name") String name){
//        return studentService.findStudentByName(name);
//    }
    @GetMapping("/studentByName")
    public List<Student> getStudentByName(@RequestParam("name") String name){
        return studentService.findStudentByName(name);
    }
//    update Student

//    @PatchMapping("/update/student")
//    public void updateStudent(@PathVariable("id") Integer id, @RequestBody UpdateStudentRequest updateStudentRequest){
//        studentService.updateStudent(id, updateStudentRequest.updateStudent());
//    }

}
