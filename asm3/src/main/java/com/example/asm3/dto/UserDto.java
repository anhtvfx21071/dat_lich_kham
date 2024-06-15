package com.example.asm3.dto;

import lombok.Data;
//Thông tin cơ bản cho admin
@Data
public class UserDto {
    private String name;
    private String email;
    private int status;

    public UserDto() {
    }

    public UserDto(String name, String email, int status) {
        this.name = name;
        this.email = email;
        this.status = status;
    }
}
