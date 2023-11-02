package com.example.demo.service;

import com.example.demo.models.Admin;
import com.example.demo.models.Student;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepository;
    public void createAdmin(Admin admin){
        adminRepository.save(admin);
    }
    public Admin findAdminById(Integer id){
        return adminRepository.findById(id).orElse(null);
    }
}
