package com.example.urlshortner.dto;

import com.example.urlshortner.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 30, message = "Name cannot exceed 30 characters")
    private String name;

    @Email(message = "Enter a valid email")
    @Size(max = 15, message = "Email must not exceed 15 characters")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String password;

    public User toUser(){
        return User.builder()
                .name(name)
                .email(email)
                .build();
    }
}
