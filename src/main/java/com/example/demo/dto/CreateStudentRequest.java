package com.example.demo.dto;

import com.example.demo.models.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStudentRequest {
    @NotBlank
    private String name;
    @NotNull
    private Integer age;
    @NotBlank
    private String email;

    public Student toStudent(){
        return Student.builder()
                .name(this.name)
                .age(this.age)
                .email(this.email)
                .build();
    }
}
