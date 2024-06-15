package com.example.asm3.dto;

import lombok.Data;

@Data
public class ResetPassword {
    private String email;
    private String password;
    private String repeatPassword;
    private String code;
}
