package com.example.demo.service;

import com.example.demo.models.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public void createStudent(Student student){
        studentRepository.save(student);
    }
    public List<Student> findStudentById(Integer id){
        Optional<Student> student = studentRepository.findById(id);
        if(student.isPresent()){
            return Arrays.asList(student.get());
        }
        else{
            return new ArrayList<>();
        }
    }
    public List<Student> findStudentByEmail(String email){
        return studentRepository.findByEmail(email);
    }
    public List<Student> findStudentByName(String name){
        return studentRepository.findByName(name);
    }
    public void updateStudent(Integer id, Student student){
        Optional<Student> studentOptional = studentRepository.findById(id);
        if(studentOptional.isPresent()){
            Student existingStudent = studentOptional.get();
            existingStudent.setName(student.getName());
            existingStudent.setEmail(student.getEmail());

            studentRepository.save(existingStudent);
        }

    }
}
