package com.example.demo.dto;

import com.example.demo.models.Student;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStudentRequest {
    private String name;
    private String email;

    public Student updateStudent(){
        return Student.builder()
                .name(this.name)
                .email(this.email)
                .build();
    }
}
