package com.example.asm3.dto;


import lombok.Data;

// đăng ký tài khoản
@Data
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private String repeatPassword;
    private String address;
    private String phone;
    private String gender;
    private String description;
    private String specialization;
    private int idDepartment;
}
