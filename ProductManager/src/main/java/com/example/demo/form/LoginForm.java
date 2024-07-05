package com.example.demo.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class LoginForm implements Serializable {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}