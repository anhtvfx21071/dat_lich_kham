package com.example.asm3.dto;

import lombok.Data;
// đăng nhập
@Data
public class SignInRequest {
    private String email;
    private String password;
}
